package com.akshita.jad.core.shell.system.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.akshita.jad.deps.org.slf4j.Logger;
import com.akshita.jad.deps.org.slf4j.LoggerFactory;
import com.akshita.jad.core.GlobalOptions;
import com.akshita.jad.core.distribution.ResultDistributor;
import com.akshita.jad.core.server.JADBootstrap;
import com.akshita.jad.core.shell.cli.CliToken;
import com.akshita.jad.core.shell.handlers.Handler;
import com.akshita.jad.core.shell.session.Session;
import com.akshita.jad.core.shell.system.Job;
import com.akshita.jad.core.shell.system.JobListener;
import com.akshita.jad.core.shell.term.Term;


/**
 * Job Controller，，，
 * 
 * @author gehui 2017731 11:55:41
 */
public class GlobalJobControllerImpl extends JobControllerImpl {
    private Map<Integer, JobTimeoutTask> jobTimeoutTaskMap = new ConcurrentHashMap<Integer, JobTimeoutTask>();
    private static final Logger logger = LoggerFactory.getLogger(GlobalJobControllerImpl.class);

    @Override
    public void close(final Handler<Void> completionHandler) {
        if (completionHandler != null) {
            completionHandler.handle(null);
        }
    }

    @Override
    public void close() {
        jobTimeoutTaskMap.clear();
        for (Job job : jobs()) {
            job.terminate();
        }
    }

    @Override
    public boolean removeJob(int id) {
        JobTimeoutTask jobTimeoutTask = jobTimeoutTaskMap.remove(id);
        if (jobTimeoutTask != null) {
            jobTimeoutTask.cancel();
        }
        return super.removeJob(id);
    }

    @Override
    public Job createJob(InternalCommandManager commandManager, List<CliToken> tokens, Session session, JobListener jobHandler, Term term, ResultDistributor resultDistributor) {
        final Job job = super.createJob(commandManager, tokens, session, jobHandler, term, resultDistributor);

        /*
         * job
         */
        JobTimeoutTask jobTimeoutTask = new JobTimeoutTask(job);
        long jobTimeoutInSecond = getJobTimeoutInSecond();
        Date timeoutDate = new Date(System.currentTimeMillis() + (jobTimeoutInSecond * 1000));
        JADBootstrap.getInstance().getScheduledExecutorService().schedule(jobTimeoutTask, jobTimeoutInSecond, TimeUnit.SECONDS);
        jobTimeoutTaskMap.put(job.id(), jobTimeoutTask);
        job.setTimeoutDate(timeoutDate);

        return job;
    }

    private long getJobTimeoutInSecond() {
        long result = -1;
        String jobTimeoutConfig = GlobalOptions.jobTimeout.trim();
        try {
            char unit = jobTimeoutConfig.charAt(jobTimeoutConfig.length() - 1);
            String duration = jobTimeoutConfig.substring(0, jobTimeoutConfig.length() - 1);
            switch (unit) {
            case 'h':
                result = TimeUnit.HOURS.toSeconds(Long.parseLong(duration));
                break;
            case 'd':
                result = TimeUnit.DAYS.toSeconds(Long.parseLong(duration));
                break;
            case 'm':
                result = TimeUnit.MINUTES.toSeconds(Long.parseLong(duration));
                break;
            case 's':
                result = Long.parseLong(duration);
                break;
            default:
                result = Long.parseLong(jobTimeoutConfig);
                break;
            }
        } catch (Throwable e) {
            logger.error("parse jobTimeoutConfig: {} error!", jobTimeoutConfig, e);
        }

        if (result < 0) {
            // ，1
            result = TimeUnit.DAYS.toSeconds(1);
            logger.warn("Configuration with job timeout " + jobTimeoutConfig + " is error, use 1d in default.");
        }
        return result;
    }

    private static class JobTimeoutTask implements Runnable {
        private Job job;

        public JobTimeoutTask(Job job) {
            this.job = job;
        }

        @Override
        public void run() {
            try {
                if (job != null) {
                    Job temp = job;
                    job = null;
                    temp.terminate();
                }
            } catch (Throwable e) {
                try {
                    logger.error("JobTimeoutTask error, job id: {}, line: {}", job.id(), job.line(), e);
                } catch (Throwable t) {
                    // ignore
                }
            }
        }

        public void cancel() {
            job = null;
        }
    }
}
