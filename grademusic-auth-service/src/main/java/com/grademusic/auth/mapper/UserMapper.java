package com.grademusic.auth.mapper;

import com.grademusic.auth.config.MapperConfig;
import com.grademusic.auth.controller.model.UserResponse;
import com.grademusic.auth.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponse toResponse(User user);
}
