package com.example.JavaSpringBoot.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {

  @Autowired MockMvc mockMvc;

  @Autowired UserService userService;

  @MockitoBean UserRepository userRepository;

  private UserCreateRequest request;
  private UserResponse response;
  private User user;

  @BeforeEach
  public void initData() {
    LocalDate dateOfBirth = LocalDate.of(2005, 7, 13);
    request =
        UserCreateRequest.builder()
            .username("npdungx")
            .firstName("dung")
            .lastName("nguyen phi")
            .password("12345678")
            .dateOfBirth(dateOfBirth)
            .build();
    response =
        UserResponse.builder()
            .id("dung123")
            .username("npdungx")
            .firstName("dung")
            .lastName("nguyen phi")
            .dateOfBirth(dateOfBirth.toString())
            .build();
    user =
        User.builder()
            .id("dung123")
            .username("npdungx")
            .firstName("dung")
            .lastName("nguyen phi")
            .dateOfBirth(dateOfBirth.toString())
            .build();
  }

  @Test
  void createUser_validRequest_success() {
    //        GIVEN
    Mockito.when(userRepository.existsByUsername(ArgumentMatchers.any())).thenReturn(false);
    Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

    //        WHEN
    var response = userService.createUser(request);

    //        THEN
    Assertions.assertThat(response.getId()).isEqualTo("dung123");
    Assertions.assertThat(response.getUsername()).isEqualTo("npdungx");
    Assertions.assertThat(response.getFirstName()).isEqualTo("dung");
    Assertions.assertThat(response.getLastName()).isEqualTo("nguyen phi");
  }

  @Test
  void createUser_userExisted_fail() {
    //        GIVEN
    Mockito.when(userRepository.existsByUsername(ArgumentMatchers.any())).thenReturn(true);
    Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);

    //        WHEN
    var exception = assertThrows(AppException.class, () -> userService.createUser(request));

    //        THEN
    Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1004);
    Assertions.assertThat(exception.getMessage()).isEqualTo("user existed");
  }

  @Test
  @WithMockUser(username = "npdungx")
  void getMyInfo_valid_success() {
    //        GIVEN
    Mockito.when(userRepository.findByUsername(ArgumentMatchers.any()))
        .thenReturn(Optional.ofNullable(user));

    //        WHEN
    var response = userService.getMyInfo();

    //        THEN
    Assertions.assertThat(response.getId()).isEqualTo("dung123");
    Assertions.assertThat(response.getUsername()).isEqualTo("npdungx");
    Assertions.assertThat(response.getFirstName()).isEqualTo("dung");
    Assertions.assertThat(response.getLastName()).isEqualTo("nguyen phi");
  }

  @Test
  @WithMockUser(username = "npdungx")
  void getMyInfo_UserNotFound_fail() {
    //        GIVEN
    Mockito.when(userRepository.findByUsername(ArgumentMatchers.any()))
        .thenReturn(Optional.empty());

    //        WHEN
    var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

    //        THEN
    Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    Assertions.assertThat(exception.getMessage()).isEqualTo("user not found");
  }
}
