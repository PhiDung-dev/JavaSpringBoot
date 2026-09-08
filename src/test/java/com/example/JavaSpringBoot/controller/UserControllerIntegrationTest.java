package com.example.JavaSpringBoot.controller;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.respose.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

  @Container static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:9.6");

  @DynamicPropertySource
  static void configureDataSource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    registry.add("spring.datasource.driverClassName", MY_SQL_CONTAINER::getDriverClassName);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
  }

  @Autowired private MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

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
}
