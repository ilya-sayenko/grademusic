package com.grademusic.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Album {

    private String id;

    private String name;

    private String artist;

    private Image image;
}
