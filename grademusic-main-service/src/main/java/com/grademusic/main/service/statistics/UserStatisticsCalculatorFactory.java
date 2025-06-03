package com.grademusic.main.service.statistics;

import com.grademusic.main.model.StatisticsType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserStatisticsCalculatorFactory {
    
    private final Map<StatisticsType, UserStatisticsCalculator> calculators;

    public UserStatisticsCalculatorFactory(Set<UserStatisticsCalculator> calculators) {
        this.calculators = calculators.stream()
                .collect(Collectors.toMap(UserStatisticsCalculator::getType, Function.identity()));
    }
    
    public UserStatisticsCalculator findCalculator(StatisticsType statisticsType) {
        return calculators.get(statisticsType);
    }
}
