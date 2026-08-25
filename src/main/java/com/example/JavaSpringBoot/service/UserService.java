package com.example.JavaSpringBoot.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.request.UserUpdateRequest;
import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.enums.Role;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.mapper.UserMapper;
import com.example.JavaSpringBoot.repository.RoleRepository;
import com.example.JavaSpringBoot.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

  UserRepository userRepository;
  RoleRepository roleRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;

  public UserResponse createUser(UserCreateRequest request) {
    log.info("Service: Create user");
    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    HashSet<String> roles = new HashSet<>();
    roles.add(Role.USER.name());
    //        user.setRoles(roles);
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }
    return userMapper.toUserResponse(user);
  }

  @PreAuthorize("hasAuthority('READ_USERS')")
  public List<UserResponse> readUsers() {
    return userMapper.toUserResponseList(userRepository.findAll());
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse readUser(String id) {
    return userMapper.toUserResponse(
        userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
  }

  public UserResponse updateUser(String id, UserUpdateRequest request) {
    User user =
        userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    userMapper.updateUser(user, request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));
    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void deleteUser(String id) {
    if (!userRepository.existsById(id)) {
      throw new AppException(ErrorCode.USER_NOT_FOUND);
    }
    userRepository.deleteById(id);
  }

  public List<User> readDetailUsers() {
    return userRepository.findAll();
  }

  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String username = context.getAuthentication().getName();
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    return userMapper.toUserResponse(user);
  }
}
