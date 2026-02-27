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
import org.springframework.http.ResponseEntity;
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
    public List<UserResponse> readUsers() {
        return userService.readUsers();
    }

    @GetMapping("/detail")
    public List<User> readDetailUser(){
        return userService.readDetailUser();
    }

    @GetMapping("/{userId}")
    public UserResponse readUser(@PathVariable("userId") String userId) {
        return userService.readUser(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId){
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1500);
        apiResponse.setMessage("user has been deleted");
        userService.deleteUser(userId);
        return ResponseEntity.accepted().body(apiResponse);
    }

}
