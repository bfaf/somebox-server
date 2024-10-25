package com.kchonov.someboxserver.config;

import com.kchonov.someboxserver.models.MoviesEntity;
import com.kchonov.someboxserver.models.MoviesMetadataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class MovieItemWriteListener implements ItemWriteListener<MoviesEntity>  {

    private final static Logger logger = LoggerFactory.getLogger(MovieItemWriteListener.class);

    @Override
    public void onWriteError(Exception ex, Chunk<? extends MoviesEntity> items) {
        logger.info("KRASIII Message: " + ex.getMessage());
        logger.info("KRASIII Class: " + ex.getClass());
//        logger.error("An error occur ", ex);
    }
}
