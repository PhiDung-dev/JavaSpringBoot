package com.example.JavaSpringBoot.mapper;

import com.example.JavaSpringBoot.dto.request.PermissionRequest;
import com.example.JavaSpringBoot.dto.respose.PermissionResponse;
import com.example.JavaSpringBoot.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
