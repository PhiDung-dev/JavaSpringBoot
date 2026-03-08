package com.example.JavaSpringBoot.controller;

import com.example.JavaSpringBoot.dto.request.AuthenticationRequest;
import com.example.JavaSpringBoot.dto.request.IntrospectRequest;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.AuthenticationResponse;
import com.example.JavaSpringBoot.dto.respose.IntrospectResponse;
import com.example.JavaSpringBoot.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login (@RequestBody AuthenticationRequest request) {
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
        return apiResponse;
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate (@RequestBody IntrospectRequest request) {
        ApiResponse<IntrospectResponse> apiResponse = ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect2(request))
                .build();
        return apiResponse;
    }
}
