package com.kchonov.someboxserver.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MovieImportBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job importMoviesJob;
    private final Job importMovieMetadataJob;

    private final Environment env;

    public MovieImportBatchScheduler(JobLauncher jobLauncher, Job importMoviesJob, Job importMovieMetadataJob, Environment env) {
        this.jobLauncher = jobLauncher;
        this.importMovieMetadataJob = importMovieMetadataJob;
        this.importMoviesJob = importMoviesJob;
        this.env = env;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // every hour
    public void performBatchJob() throws Exception {
        String profile =  env.getDefaultProfiles().length > 0 ? env.getDefaultProfiles()[0] : null;
        boolean runImport = profile != null && profile.compareTo("default") != 0;
        if (runImport) {
            // Run only in production
            JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(importMoviesJob, params);
        jobLauncher.run(importMovieMetadataJob, params);
        }
    }
}
