package com.example.springbatch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

@Component
public class BatchScheduler {
    private JobLauncher jobLauncher;
    private Job job;

    @Scheduled(fixedDelay = 1000, initialDelay = 2000)
    public void runJob() {
        try {
            System.out.println("delay");
            jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
