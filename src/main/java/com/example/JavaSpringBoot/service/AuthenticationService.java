package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.AuthenticationRequest;
import com.example.JavaSpringBoot.dto.respose.ApiResponse;
import com.example.JavaSpringBoot.dto.respose.AuthenticationResponse;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .authenticate(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .build();
        return authenticationResponse;
    }
}
