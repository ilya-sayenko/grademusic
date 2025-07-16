package com.grademusic.main.model.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStatisticsByReviewsImpl implements UserStatisticsByReviews {

    private Long userId;

    private Long countOfReviews;
}
