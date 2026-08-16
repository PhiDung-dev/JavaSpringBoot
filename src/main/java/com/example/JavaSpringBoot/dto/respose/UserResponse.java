package com.example.JavaSpringBoot.dto.respose;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

  String id;
  String username;
  String password;
  String firstName;
  String lastName;
  String dateOfBirth;
  Set<RoleResponse> roles;
}
