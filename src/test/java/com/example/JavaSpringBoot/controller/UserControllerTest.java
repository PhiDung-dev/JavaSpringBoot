package com.example.JavaSpringBoot.controller;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.example.JavaSpringBoot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean UserService userService;

  private UserCreateRequest request;
  private UserResponse response;

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
  }

  @Test
  void createUser_validRequest_success() throws Exception {
    //        GIVEN
    String request = objectMapper.writeValueAsString(this.request);
    Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(this.response);

    //        WHEN
    var result =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

    //        THEN
    result
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
  }

  @Test
  void createUser_usernameInvalid_fail() throws Exception {
    //        GIVEN
    request.setUsername("dung");
    String request = objectMapper.writeValueAsString(this.request);

    //        WHEN
    var result =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

    //        THEN
    result
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("code").value(1002))
        .andExpect(
            MockMvcResultMatchers.jsonPath("message")
                .value("username must be at least 6 characters"));
  }
}
