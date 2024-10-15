package com.kchonov.someboxserver.config;

import com.google.gson.Gson;
import com.kchonov.someboxserver.entities.SomeBoxFileInfo;
import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.models.MoviesMetadataEntity;
import com.kchonov.someboxserver.repository.MoviesEntityRepository;
import com.kchonov.someboxserver.services.FilesService;
import jakarta.persistence.EntityManagerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestClient;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

import static org.springframework.http.MediaType.*;

@Configuration
@EnableBatchProcessing
public class MovieMetadataImportBatchConfig {
    private final MoviesEntityRepository moviesEntityRepository;
    private final EntityManagerFactory entityManagerFactory;

    private final RestClient customClient;

    private final DataSource dataSource;

    public MovieMetadataImportBatchConfig(MoviesEntityRepository moviesEntityRepository, EntityManagerFactory entityManagerFactory, DataSource dataSource) {
        this.moviesEntityRepository = moviesEntityRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.dataSource = dataSource;

        customClient = RestClient.builder().build();
    }

    @Bean
    public Step importStep1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcCursorItemReader metadataReader,
                      ItemProcessor<MoviesEntity, MoviesMetadataEntity> metadataProcessor,
                      ItemWriter<MoviesMetadataEntity> metadataWriter) {
        StepBuilder stepbuilder = new StepBuilder("importStep1", jobRepository);

        return stepbuilder.<MoviesEntity, MoviesMetadataEntity>chunk(10, transactionManager)
                .reader(metadataReader)
                .processor(metadataProcessor)
                .writer(metadataWriter)
                .faultTolerant()
                .skip(org.hibernate.exception.ConstraintViolationException.class)
                .skipLimit(20)
                .listener(new MovieMetadataItemWriteListener())
                .build();
    }

    @Bean
    public Job importMovieMetadataJob(JobRepository jobRepository, Step importStep1) {
        return new JobBuilder("importMovieMetadataJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importStep1)
                .build();
    }

    @Bean
    public JdbcCursorItemReader metadataReader() {
        JdbcCursorItemReader itemReader = new JdbcCursorItemReader();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("SELECT * FROM movies");
        itemReader.setRowMapper(new MoviesRowMapper());

        return itemReader;
    }

    @Bean
    public ItemProcessor<MoviesEntity, MoviesMetadataEntity> metadataProcessor() {
        return movie -> {
            System.out.println("Processing: " + movie.getName());
            String url = String.format("https://www.omdbapi.com/?apikey=eca5f42&t=%s&y=%s", movie.getName(), movie.getReleaseYear());
            String dataResponse = customClient.get()
                    .uri(url)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> status.value() != 200, (request, response) -> System.out.println(String.format("Unable to find data for %s %s", movie.getName(), movie.getReleaseYear())))
                    .body(String.class);
            OMDBResponseEntity o = new Gson().fromJson(dataResponse, OMDBResponseEntity.class);

            // TODO: this should be somewhere else
            String cover = "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";
            String imageUrl = o.getPoster() != null && o.getPoster().length() > 0 ? o.getPoster() : null;
            if (imageUrl != null) {
                byte[] imageData = customClient.get()
                        .uri(imageUrl)
                        .accept(IMAGE_JPEG)
                        .exchange((request, response) -> {
                            if (response.getStatusCode().is4xxClientError()) {
                                throw new IOException("Unable to fetch image for movie: " + movie.getName());
                            }
                            else {
                                BufferedImage img = ImageIO.read(response.getBody());
                                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                                ImageIO.write(img, "jpg", bao);
                                return bao.toByteArray();
                            }
                        });
                String bb = Base64.getEncoder().encodeToString(imageData);
                cover = bb;
            }

            Thread.sleep(2 * 1000);
            // movie.getFilename()
//            System.out.println(o.getRated());

            MoviesMetadataEntity m = new MoviesMetadataEntity();
            m.setMovieId(movie.getMovieId());
            m.setPlot(o.getPlot());
            m.setRating(o.getImdbRating());
            m.setDuration(0L);
            m.setPoster(cover);
            m.setRated(o.getImdbRating());
            m.setGanre(o.getGenre());
            m.setSkipIntroAt(0L);
            m.setFormat("1080p");
            m.setSkipCreditsAt(0L);
            m.setPlayCount(0L);
            m.setSkipIntroDuration(0L);

            return m;
        };
    }

    @Bean
    public JpaItemWriter<MoviesMetadataEntity> metadataWriter() {
        JpaItemWriter<MoviesMetadataEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
