package com.kchonov.someboxserver.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BasicSomeBoxFileInfo {

    private Long id;
    private String originalFilename;

    @Override
    public String toString() {
        return "Id: " + id + "\n originalFilename: " + originalFilename;
    }
}
