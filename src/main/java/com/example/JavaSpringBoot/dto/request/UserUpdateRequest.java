package com.example.JavaSpringBoot.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    String dateOfBirth;
    List<String> roles;

}
