package com.grademusic.main.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Artist {

    private String id;

    private String name;
}
