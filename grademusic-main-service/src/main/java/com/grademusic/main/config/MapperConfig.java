package com.grademusic.main.config;

import org.mapstruct.extensions.spring.SpringMapperConfig;

@org.mapstruct.MapperConfig(componentModel = "spring")
@SpringMapperConfig(generateConverterScan = true)
public interface MapperConfig {}
