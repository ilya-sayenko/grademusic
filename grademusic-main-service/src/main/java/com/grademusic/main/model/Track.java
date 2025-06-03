package com.grademusic.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Track {

    private String id;

    private String name;

    private int duration;

    private int order;
}
