package com.example.JavaSpringBoot.dto.respose;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String username;
    String password;
    String firstName;
    String lastName;
    String dateOfBirth;
    Set<String> roles;
}
