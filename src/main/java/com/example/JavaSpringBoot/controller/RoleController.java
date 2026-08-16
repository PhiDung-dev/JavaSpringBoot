package com.example.JavaSpringBoot.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.JavaSpringBoot.dto.request.RoleRequest;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.RoleResponse;
import com.example.JavaSpringBoot.service.RoleService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

  RoleService roleService;

  @PostMapping
  public ApiResponse<RoleResponse> createPermission(@RequestBody @Valid RoleRequest request) {
    ApiResponse<RoleResponse> apiResponse =
        ApiResponse.<RoleResponse>builder().result(roleService.createRole(request)).build();
    return apiResponse;
  }

  @GetMapping
  public ApiResponse<List<RoleResponse>> readUsers() {
    ApiResponse<List<RoleResponse>> apiResponse =
        ApiResponse.<List<RoleResponse>>builder().result(roleService.readRoles()).build();
    return apiResponse;
  }

  @DeleteMapping("/{role}")
  public ApiResponse<Void> deleteUser(@PathVariable String role) {
    roleService.deleteRole(role);
    ApiResponse<Void> apiResponse =
        ApiResponse.<Void>builder().message("user has been deleted").build();
    return apiResponse;
  }
}
