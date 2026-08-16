package com.example.JavaSpringBoot.controller;

import java.text.ParseException;
import org.springframework.web.bind.annotation.*;
import com.example.JavaSpringBoot.dto.request.AuthenticationRequest;
import com.example.JavaSpringBoot.dto.request.IntrospectRequest;
import com.example.JavaSpringBoot.dto.request.LogoutRequest;
import com.example.JavaSpringBoot.dto.request.RefreshRequest;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.AuthenticationResponse;
import com.example.JavaSpringBoot.dto.respose.IntrospectResponse;
import com.example.JavaSpringBoot.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  AuthenticationService authenticationService;

  @PostMapping("/login")
  ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
    ApiResponse<AuthenticationResponse> apiResponse =
        ApiResponse.<AuthenticationResponse>builder()
            .result(authenticationService.authenticate(request))
            .build();
    return apiResponse;
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
      throws ParseException, JOSEException {
    ApiResponse<IntrospectResponse> apiResponse =
        ApiResponse.<IntrospectResponse>builder()
            .result(authenticationService.introspect(request))
            .build();
    return apiResponse;
  }

  @PostMapping("/logout")
  ApiResponse<Void> logout(@RequestBody LogoutRequest request)
      throws ParseException, JOSEException {
    authenticationService.logout(request);
    return ApiResponse.<Void>builder().build();
  }

  @PostMapping("/refreshToken")
  ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request)
      throws ParseException, JOSEException {
    return ApiResponse.<AuthenticationResponse>builder()
        .result(authenticationService.refreshToken(request))
        .build();
  }
}
