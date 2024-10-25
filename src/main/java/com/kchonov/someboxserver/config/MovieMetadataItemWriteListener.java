package com.kchonov.someboxserver.config;

import com.kchonov.someboxserver.models.MoviesMetadataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class MovieMetadataItemWriteListener implements ItemWriteListener<MoviesMetadataEntity> {

    private final static Logger logger = LoggerFactory.getLogger(MovieMetadataItemWriteListener.class);

    @Override
    public void onWriteError(Exception ex, Chunk<? extends MoviesMetadataEntity> items) {
        logger.info("KRASIII Message: " + ex.getMessage());
        logger.info("KRASIII Class: " + ex.getClass());
//        logger.error("An error occur ", ex);
    }
}
