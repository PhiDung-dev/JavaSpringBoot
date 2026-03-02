package com.example.JavaSpringBoot.controller;

import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.service.UserService;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.request.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> readUsers() {
        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .result(userService.readUsers())
                .build();
        return apiResponse;
    }

    @GetMapping("/detail")
    public ApiResponse<List<User>> readDetailUser(){
        ApiResponse<List<User>> apiResponse = ApiResponse.<List<User>>builder()
                .result(userService.readDetailUsers())
                .build();
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> readUser(@PathVariable("userId") String userId) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(userService.readUser(userId))
                .build();
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("user has been deleted")
                .build();
        return apiResponse;
    }

}
