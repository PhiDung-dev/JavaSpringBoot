package com.example.JavaSpringBoot.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.JavaSpringBoot.dto.request.PermissionRequest;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.PermissionResponse;
import com.example.JavaSpringBoot.service.PermissionService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

  PermissionService permissionService;

  @PostMapping
  public ApiResponse<PermissionResponse> createPermission(
      @RequestBody @Valid PermissionRequest request) {
    ApiResponse<PermissionResponse> apiResponse =
        ApiResponse.<PermissionResponse>builder()
            .result(permissionService.createPermission(request))
            .build();
    return apiResponse;
  }

  @GetMapping
  public ApiResponse<List<PermissionResponse>> readUsers() {
    ApiResponse<List<PermissionResponse>> apiResponse =
        ApiResponse.<List<PermissionResponse>>builder()
            .result(permissionService.readPermissions())
            .build();
    return apiResponse;
  }

  @DeleteMapping("/{permission}")
  public ApiResponse<Void> deleteUser(@PathVariable String permission) {
    permissionService.deletePermission(permission);
    ApiResponse<Void> apiResponse =
        ApiResponse.<Void>builder().message("user has been deleted").build();
    return apiResponse;
  }
}
