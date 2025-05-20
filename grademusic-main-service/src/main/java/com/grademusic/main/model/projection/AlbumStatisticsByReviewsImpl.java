package com.grademusic.main.model.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlbumStatisticsByReviewsImpl implements AlbumStatisticsByReviews {

    private String albumId;

    private Long countOfReviews;
}
