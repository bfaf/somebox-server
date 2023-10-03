package com.kchonov.someboxserver.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SomeBoxFileInfo {

    private Long id;
    private String filename;
    private String originalFilename;
    private String screenshotName;
    private Long duration;
    private String readableDuration;
    private Long resumeFromTime;
}
