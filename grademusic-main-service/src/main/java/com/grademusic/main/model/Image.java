package com.grademusic.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Image {

    private String small;

    private String medium;

    private String large;

    private String extralarge;
}
