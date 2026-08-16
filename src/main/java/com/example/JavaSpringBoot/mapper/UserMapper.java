package com.example.JavaSpringBoot.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.request.UserUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.example.JavaSpringBoot.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toUser(UserCreateRequest request);

  UserResponse toUserResponse(User user);

  List<UserResponse> toUserResponseList(List<User> users);

  @Mapping(target = "roles", ignore = true)
  void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
