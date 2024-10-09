package com.kchonov.someboxserver.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MovieImportBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job importMoviesJob;
    private final Job importMovieMetadataJob;

    public MovieImportBatchScheduler(JobLauncher jobLauncher, Job importMoviesJob, Job importMovieMetadataJob) {
        this.jobLauncher = jobLauncher;
        this.importMovieMetadataJob = importMovieMetadataJob;
        this.importMoviesJob = importMoviesJob;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // every hour
    public void performBatchJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
//        jobLauncher.run(importMoviesJob, params);
//        jobLauncher.run(importMovieMetadataJob, params);
    }
}
