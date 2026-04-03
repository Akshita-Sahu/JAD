package com.akshita.jad.core.distribution.impl;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.command.model.InputStatusModel;
import com.akshita.jad.core.command.model.MessageModel;
import com.akshita.jad.core.command.model.ResultModel;
import com.akshita.jad.core.distribution.DistributorOptions;
import com.akshita.jad.core.distribution.ResultConsumer;
import com.akshita.jad.core.distribution.SharingResultDistributor;
import com.akshita.jad.core.shell.session.Session;
import com.akshita.jad.core.shell.system.Job;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SharingResultDistributorImpl implements SharingResultDistributor {
    private static final Logger logger = LoggerFactory.getLogger(SharingResultDistributorImpl.class);

    private List<ResultConsumer> consumers = new CopyOnWriteArrayList<ResultConsumer>();
    private BlockingQueue<ResultModel> pendingResultQueue = new ArrayBlockingQueue<ResultModel>(10);
    private final Session session;
    private Thread distributorThread;
    private volatile boolean running;
    private AtomicInteger consumerNumGenerator = new AtomicInteger(0);

    private SharingResultConsumerImpl sharingResultConsumer = new SharingResultConsumerImpl();
    
    // ，
    private volatile boolean interruptedForUnhealthy = false;

    public SharingResultDistributorImpl(Session session) {
        this.session = session;
        this.running = true;
        distributorThread = new Thread(new DistributorTask(), "ResultDistributor");
        distributorThread.setDaemon(true);  // ， JVM 
        distributorThread.start();
    }

    @Override
    public void appendResult(ResultModel result) {
        //
        try {
            if (!pendingResultQueue.offer(result, 100, TimeUnit.MILLISECONDS)) {
                ResultModel discardResult = pendingResultQueue.poll();
                // ，distribute 
                // ，
                interruptJob("result queue is full: "+ pendingResultQueue.size());
            }
        } catch (InterruptedException e) {
            //ignore
        }
    }

    private void interruptJob(String message) {
        Job job = session.getForegroundJob();
        if (job != null) {
            logger.warn(message+", current job was interrupted.", job.id());
            job.interrupt();
            pendingResultQueue.offer(new MessageModel(message+", current job was interrupted."));
        }
    }

    private void distribute() {
        while (running) {
            try {
                ResultModel result = pendingResultQueue.poll(100, TimeUnit.MILLISECONDS);
                if (result != null) {
                    sharingResultConsumer.appendResult(result);
                    
                    //  consumer，
                    if (consumers.isEmpty()) {
                        continue;
                    }
                    
                    //consumer
                    int healthCount = 0;
                    for (int i = 0; i < consumers.size(); i++) {
                        ResultConsumer consumer = consumers.get(i);
                        if(consumer.isHealthy()){
                            healthCount += 1;
                        }
                        consumer.appendResult(result);
                    }
                    //consumer，
                    //
                    if (healthCount == 0 && !interruptedForUnhealthy) {
                        interruptedForUnhealthy = true;
                        interruptJob("all consumers are unhealthy");
                    }
                } else {
                    // ，consumer，
                    if (interruptedForUnhealthy) {
                        for (int i = 0; i < consumers.size(); i++) {
                            if (consumers.get(i).isHealthy()) {
                                interruptedForUnhealthy = false;
                                break;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                // ，
                Thread.currentThread().interrupt();
                break;
            } catch (Throwable e) {
                logger.warn("distribute result failed: " + e.getMessage(), e);
            }
        }
        logger.debug("ResultDistributor thread exited");
    }

    @Override
    public void close() {
        this.running = false;
        
        // ， poll 
        if (distributorThread != null) {
            distributorThread.interrupt();
        }
        
        //  consumers
        for (ResultConsumer consumer : consumers) {
            try {
                consumer.close();
            } catch (Exception e) {
                logger.warn("close consumer failed: " + e.getMessage(), e);
            }
        }
        consumers.clear();
        
        // 
        pendingResultQueue.clear();
    }

    @Override
    public void addConsumer(ResultConsumer consumer) {
        int consumerNo = consumerNumGenerator.incrementAndGet();
        String consumerId = UUID.randomUUID().toString().replaceAll("-", "") + "_" + consumerNo;
        consumer.setConsumerId(consumerId);

        //
        sharingResultConsumer.copyTo(consumer);

        consumers.add(consumer);
    }

    @Override
    public void removeConsumer(ResultConsumer consumer) {
        consumers.remove(consumer);
        consumer.close();
    }

    @Override
    public List<ResultConsumer> getConsumers() {
        return consumers;
    }

    @Override
    public ResultConsumer getConsumer(String consumerId) {
        for (int i = 0; i < consumers.size(); i++) {
            ResultConsumer consumer = consumers.get(i);
            if (consumer.getConsumerId().equals(consumerId)) {
                return consumer;
            }
        }
        return null;
    }

    private class DistributorTask implements Runnable {
        @Override
        public void run() {
            distribute();
        }
    }

    private static class SharingResultConsumerImpl implements ResultConsumer {
        private BlockingQueue<ResultModel> resultQueue = new ArrayBlockingQueue<ResultModel>(DistributorOptions.resultQueueSize);
        private ReentrantLock queueLock = new ReentrantLock();
        private InputStatusModel lastInputStatus;

        @Override
        public boolean appendResult(ResultModel result) {
            queueLock.lock();
            try {
                //，
                if (result instanceof InputStatusModel) {
                    lastInputStatus = (InputStatusModel) result;
                    return true;
                }
                while (!resultQueue.offer(result)) {
                    ResultModel discardResult = resultQueue.poll();
                }
            } finally {
                if (queueLock.isHeldByCurrentThread()) {
                    queueLock.unlock();
                }
            }
            return true;
        }

        public void copyTo(ResultConsumer consumer) {
            //，，，
            queueLock.lock();
            try {
                for (ResultModel result : resultQueue) {
                    consumer.appendResult(result);
                }
                //
                if (lastInputStatus != null) {
                    consumer.appendResult(lastInputStatus);
                }
            } finally {
                if (queueLock.isHeldByCurrentThread()) {
                    queueLock.unlock();
                }
            }
        }

        @Override
        public List<ResultModel> pollResults() {
            return null;
        }

        @Override
        public long getLastAccessTime() {
            return 0;
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public boolean isPolling() {
            return false;
        }

        @Override
        public String getConsumerId() {
            return "shared-consumer";
        }

        @Override
        public void setConsumerId(String consumerId) {
        }

        @Override
        public boolean isHealthy() {
            return true;
        }
    }
}
