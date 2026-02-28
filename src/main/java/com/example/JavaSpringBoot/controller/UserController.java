package com.example.JavaSpringBoot.controller;

import com.example.JavaSpringBoot.dto.request.ApiResponse;
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
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> readUsers() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.readUsers());
        return apiResponse;
    }

    @GetMapping("/detail")
    public ApiResponse<List<User>> readDetailUser(){
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.readDetailUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> readUser(@PathVariable("userId") String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.readUser(userId));
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, request));
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        userService.deleteUser(userId);
        apiResponse.setMessage("user has been deleted");
        return apiResponse;
    }

}
