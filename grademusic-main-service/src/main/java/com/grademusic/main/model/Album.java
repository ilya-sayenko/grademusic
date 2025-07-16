package com.grademusic.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Album {

    private String id;

    private String name;

    private String artist;

    private Double grade;

    private Image image;

    private List<Track> tracks;
}
