package unittest.grpc;

import jad.grpc.unittest.JADUnittest;
import jad.grpc.unittest.JADUnittestServiceGrpc;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.akshita.jad.grpc.server.JADGrpcServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: FengYe
 * @date: 2024/9/24 00:17
 * @description: GrpcUnaryTest
 */
public class GrpcTest {
    private static final String HOST = "localhost";
    private static final int PORT = 9092;
    private static final String HOST_PORT = HOST + ":" + PORT;
    private static final String UNIT_TEST_GRPC_SERVICE_PACKAGE_NAME = "unittest.grpc.service.impl";
    private static final Logger log = (Logger) LoggerFactory.getLogger(GrpcTest.class);
    private ManagedChannel clientChannel;
    Random random = new Random();
    ExecutorService threadPool = Executors.newFixedThreadPool(10);


    @Before
    public void startServer() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("ROOT");

        rootLogger.setLevel(Level.INFO);

        Thread grpcWebProxyStart = new Thread(() -> {
            JADGrpcServer jadGrpcServer = new JADGrpcServer(PORT, UNIT_TEST_GRPC_SERVICE_PACKAGE_NAME);
            jadGrpcServer.start();
        });
        grpcWebProxyStart.start();

        clientChannel = ManagedChannelBuilder.forTarget(HOST_PORT)
                .usePlaintext()
                .build();
    }

    @Test
    public void testUnary() {
        log.info("testUnary start!");


        JADUnittestServiceGrpc.JADUnittestServiceBlockingStub stub = JADUnittestServiceGrpc.newBlockingStub(clientChannel);

        try {
            JADUnittest.JADUnittestRequest request = JADUnittest.JADUnittestRequest.newBuilder().setMessage("unaryInvoke").build();
            JADUnittest.JADUnittestResponse res = stub.unary(request);
            System.out.println(res.getMessage());
        } finally {
            clientChannel.shutdownNow();
        }
        log.info("testUnary success!");
    }

    @Test
    public void testUnarySum() throws InterruptedException {
        log.info("testUnarySum start!");

        JADUnittestServiceGrpc.JADUnittestServiceBlockingStub stub = JADUnittestServiceGrpc.newBlockingStub(clientChannel);
        for (int i = 0; i < 10; i++) {
            AtomicInteger sum = new AtomicInteger(0);
            int finalId = i;
            for (int j = 0; j < 10; j++) {
                int num = random.nextInt(101);
                sum.addAndGet(num);
                threadPool.submit(() -> {
                    addSum(stub, finalId, num);
                });
            }
            Thread.sleep(2000L);
            int grpcSum = getSum(stub, finalId);
            System.out.println("id:" + finalId + ",sum:" + sum.get() + ",grpcSum:" + grpcSum);
            Assert.assertEquals(sum.get(), grpcSum);
        }
        clientChannel.shutdown();
        log.info("testUnarySum success!");
    }

    // 
    @Test
    public void testClientStreamSum() throws Throwable {
        log.info("testClientStreamSum start!");

        JADUnittestServiceGrpc.JADUnittestServiceStub stub = JADUnittestServiceGrpc.newStub(clientChannel);

        AtomicInteger sum = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<JADUnittest.JADUnittestRequest> clientStreamObserver = stub.clientStreamSum(new StreamObserver<JADUnittest.JADUnittestResponse>() {
            @Override
            public void onNext(JADUnittest.JADUnittestResponse response) {
                System.out.println("local sum:" + sum + ", grpc sum:" + response.getNum());
                Assert.assertEquals(sum.get(), response.getNum());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t);
            }

            @Override
            public void onCompleted() {
                System.out.println("testClientStreamSum completed.");
                latch.countDown();
            }
        });

        for (int j = 0; j < 100; j++) {
            int num = random.nextInt(1001);
            sum.addAndGet(num);
            clientStreamObserver.onNext(JADUnittest.JADUnittestRequest.newBuilder().setNum(num).build());
        }

        clientStreamObserver.onCompleted();
        latch.await(20,TimeUnit.SECONDS);
        clientChannel.shutdown();
        log.info("testClientStreamSum success!");
    }

    // 
    @Test
    public void testDataIsolation() throws InterruptedException {
        log.info("testDataIsolation start!");

        JADUnittestServiceGrpc.JADUnittestServiceStub stub = JADUnittestServiceGrpc.newStub(clientChannel);
        for (int i = 0; i < 10; i++) {
            threadPool.submit(() -> {
                AtomicInteger sum = new AtomicInteger(0);
                CountDownLatch latch = new CountDownLatch(1);
                StreamObserver<JADUnittest.JADUnittestRequest> clientStreamObserver = stub.clientStreamSum(new StreamObserver<JADUnittest.JADUnittestResponse>() {
                    @Override
                    public void onNext(JADUnittest.JADUnittestResponse response) {
                        System.out.println("local sum:" + sum + ", grpc sum:" + response.getNum());
                        Assert.assertEquals(sum.get(), response.getNum());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Error: " + t);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("testDataIsolation completed.");
                        latch.countDown();
                    }
                });

                for (int j = 0; j < 5; j++) {
                    int num = random.nextInt(101);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sum.addAndGet(num);
                    clientStreamObserver.onNext(JADUnittest.JADUnittestRequest.newBuilder().setNum(num).build());
                }

                clientStreamObserver.onCompleted();
                try {
                    latch.await(20,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientChannel.shutdown();
            });
        }
        Thread.sleep(10000L);
        log.info("testDataIsolation success!");
    }

    @Test
    public void testServerStream() throws InterruptedException {
        log.info("testServerStream start!");

        JADUnittestServiceGrpc.JADUnittestServiceStub stub = JADUnittestServiceGrpc.newStub(clientChannel);

        JADUnittest.JADUnittestRequest request = JADUnittest.JADUnittestRequest.newBuilder().setMessage("serverStream").build();

        stub.serverStream(request, new StreamObserver<JADUnittest.JADUnittestResponse>() {
            @Override
            public void onNext(JADUnittest.JADUnittestResponse value) {
                System.out.println("testServerStream client receive: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                System.out.println("testServerStream completed");
            }
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientChannel.shutdown();
        }
        log.info("testServerStream success!");
    }

    // 
    @Test
    public void testBiStream() throws Throwable {
        log.info("testBiStream start!");

        JADUnittestServiceGrpc.JADUnittestServiceStub stub = JADUnittestServiceGrpc.newStub(clientChannel);

        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<JADUnittest.JADUnittestRequest> biStreamObserver = stub.biStream(new StreamObserver<JADUnittest.JADUnittestResponse>() {
            @Override
            public void onNext(JADUnittest.JADUnittestResponse response) {
                System.out.println("testBiStream receive: "+response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t);
            }

            @Override
            public void onCompleted() {
                System.out.println("testBiStream completed.");
                latch.countDown();
            }
        });

        String[] messages = new String[]{"testBiStream1","testBiStream2","testBiStream3"};
        for (String msg : messages) {
            JADUnittest.JADUnittestRequest request = JADUnittest.JADUnittestRequest.newBuilder().setMessage(msg).build();
            biStreamObserver.onNext(request);
        }

        Thread.sleep(2000);
        biStreamObserver.onCompleted();
        latch.await(20, TimeUnit.SECONDS);
        clientChannel.shutdown();
        log.info("testBiStream success!");
    }

    private void addSum(JADUnittestServiceGrpc.JADUnittestServiceBlockingStub stub, int id, int num) {
        JADUnittest.JADUnittestRequest request = JADUnittest.JADUnittestRequest.newBuilder().setId(id).setNum(num).build();
        JADUnittest.JADUnittestResponse res = stub.unaryAddSum(request);
    }

    private int getSum(JADUnittestServiceGrpc.JADUnittestServiceBlockingStub stub, int id) {
        JADUnittest.JADUnittestRequest request = JADUnittest.JADUnittestRequest.newBuilder().setId(id).build();
        JADUnittest.JADUnittestResponse res = stub.unaryGetSum(request);
        return res.getNum();
    }
}
