package com.example.JavaSpringBoot.mapper;

import com.example.JavaSpringBoot.dto.request.RoleRequest;
import com.example.JavaSpringBoot.dto.respose.RoleResponse;
import com.example.JavaSpringBoot.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
