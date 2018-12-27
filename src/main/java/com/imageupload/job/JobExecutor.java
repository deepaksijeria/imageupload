package com.imageupload.job;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * It add jobId to blocking queue for asynchronous processing
 * It create a thread to read from blocking queue to process the job.
 */
@Component
public class JobExecutor {

    private static BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Add job Id to queue
     * @param jobId String
     */
    public void add(String jobId) {
        queue.add(jobId);
    }

    /**
     * This method get initialized during the startup
     * It submit the job to process the job queue
     */
    @PostConstruct
    public void executeJob() {
        Runnable jobRunnable = new Runnable() {
            @Override
            public void run() {
                ExecutorService executor = Executors.newFixedThreadPool(5);
                try {
                    while (true) {
                        String jobId = queue.take();
                        System.out.println("Executing Job : " + jobId);
                        executor.submit(new ImageJobProcessor(jobId));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    executor.shutdown();
                }
            }
        };


        executor.submit(jobRunnable);

    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }


}
