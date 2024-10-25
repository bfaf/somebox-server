package com.kchonov.someboxserver.batch;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.utilities.FileUtilities;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class MovieImportBatchConfig {

    Logger logger = LoggerFactory.getLogger(MovieImportBatchConfig.class);

    private final SomeBoxConfig someBoxConfig;
    private final EntityManagerFactory entityManagerFactory;

    public MovieImportBatchConfig(SomeBoxConfig someBoxConfig, EntityManagerFactory entityManagerFactory) {
        this.someBoxConfig = someBoxConfig;
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

    public List<String> listFiles() {
        final Map<String, Boolean> FILE_FORMATS = Map.of(".mp4", true, ".mkv", true);
        List<String> files = Stream.of(new File(someBoxConfig.sourceDir()).listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> FILE_FORMATS.containsKey(FileUtilities.getExtension(file.getName())))
                .map(File::getName)
                .collect(Collectors.toList());

        return files;
    }

    @Bean
    public ItemReader<MoviesEntity> reader() {
        List<String> files = this.listFiles();
        List<MoviesEntity> movies = new ArrayList<>();
        for (String f : files) {
            String[] parts = f.split("-");
            movies.add(new MoviesEntity(
                    parts[0],
                    parts[1],
                    f
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
