package com.example.JavaSpringBoot.service;

import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.JavaSpringBoot.dto.request.RoleRequest;
import com.example.JavaSpringBoot.dto.respose.RoleResponse;
import com.example.JavaSpringBoot.entity.Permission;
import com.example.JavaSpringBoot.entity.Role;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.mapper.RoleMapper;
import com.example.JavaSpringBoot.repository.PermissionRepository;
import com.example.JavaSpringBoot.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  RoleMapper roleMapper;

  public RoleResponse createRole(RoleRequest request) {
    if (roleRepository.existsById(request.getName())) {
      throw new AppException(ErrorCode.ROLE_EXISTED);
    }
    Role role = roleMapper.toRole(request);
    List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
    role.setPermissions(new HashSet<>(permissions));
    return roleMapper.toRoleResponse(roleRepository.save(role));
  }

  public List<RoleResponse> readRoles() {
    List<Role> roles = roleRepository.findAll();
    return roles.stream().map(roleMapper::toRoleResponse).toList();
  }

  public void deleteRole(String name) {
    if (!roleRepository.existsById(name)) {
      throw new AppException(ErrorCode.ROLE_NOT_FOUND);
    }
    roleRepository.deleteById(name);
  }
}
