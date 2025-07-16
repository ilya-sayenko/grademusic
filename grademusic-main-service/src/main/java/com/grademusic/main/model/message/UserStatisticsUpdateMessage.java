package com.grademusic.main.model.message;

import com.grademusic.main.model.StatisticsType;
import lombok.Builder;

@Builder
public record UserStatisticsUpdateMessage(
        Long userId,
        StatisticsType statisticsType
) {
}
