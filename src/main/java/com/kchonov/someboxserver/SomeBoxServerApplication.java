package com.kchonov.someboxserver;

import com.kchonov.someboxserver.config.SomeBoxConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(SomeBoxConfig.class)
@EnableCaching
public class SomeBoxServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomeBoxServerApplication.class, args);
	}
}
