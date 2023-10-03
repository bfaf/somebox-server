package com.kchonov.someboxserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("somebox.config")
public record SomeBoxConfig(String sourceDir, String screenshotDir, int screenshotWidth, int screenshotHeight) {
}
