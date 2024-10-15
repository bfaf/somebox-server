package com.kchonov.someboxserver.config;

import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.services.FilesService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class MovieImportBatchConfig {

    private final FilesService fileService;
    private final EntityManagerFactory entityManagerFactory;

    public MovieImportBatchConfig(FilesService fileService, EntityManagerFactory entityManagerFactory) {
        this.fileService = fileService;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      ItemReader<MoviesEntity> reader,
                      ItemProcessor<MoviesEntity, MoviesEntity> processor,
                      ItemWriter<MoviesEntity> writer) {
        StepBuilder stepbuilder = new StepBuilder("step1", jobRepository);

        return stepbuilder.<MoviesEntity, MoviesEntity>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(org.hibernate.exception.ConstraintViolationException.class)
                .skipLimit(20)
                .listener(new MovieItemWriteListener())
                .build();
    }

    @Bean
    public Job importMoviesJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importMoviesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public ItemReader<MoviesEntity> reader() {
        List<SomeBoxFileInfo> files = this.fileService.listFiles();
        List<MoviesEntity> movies = new ArrayList<>();
        for (SomeBoxFileInfo f : files) {
            String[] parts = f.getFilename().split("-");
            movies.add(new MoviesEntity(
                    parts[0],
                    parts[1],
                    f.getOriginalFilename()
            ));
        }

        return new CustomItemReader<>(movies);
    }

    @Bean
    public ItemProcessor<MoviesEntity, MoviesEntity> processor() {
        return movie -> {
            movie.setName(movie.getName());
            movie.setFilename(movie.getFilename());
            movie.setReleaseYear(movie.getReleaseYear());
            return movie;
        };
    }

    @Bean
    public JpaItemWriter<MoviesEntity> writer() {
        JpaItemWriter<MoviesEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
