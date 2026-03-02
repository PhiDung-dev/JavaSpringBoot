package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.mapper.UserMapper;
import com.example.JavaSpringBoot.repository.UserRepository;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.request.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> readUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    public UserResponse readUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        if(!userRepository.existsById(id)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public List<User> readDetailUsers() {
        return userRepository.findAll();
    }
}