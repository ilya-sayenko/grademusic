package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AlbumStatisticsCalculatorFactory {

    private final Map<StatisticsType, AlbumStatisticsCalculator> calculators;

    public AlbumStatisticsCalculatorFactory(Set<AlbumStatisticsCalculator> calculators) {
        this.calculators = calculators.stream()
                .collect(Collectors.toMap(AlbumStatisticsCalculator::getType, Function.identity()));
    }

    public AlbumStatisticsCalculator findCalculator(StatisticsType statisticsType) {
        return calculators.get(statisticsType);
    }
}
