package com.grademusic.main.model.message;

import com.grademusic.main.model.StatisticsType;
import lombok.Builder;

@Builder
public record AlbumStatisticsUpdateMessage(
        String albumId,
        StatisticsType statisticsType
) {
}
