package com.example.JavaSpringBoot.dto.request;

import com.example.JavaSpringBoot.validator.DateOfBirthConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {

    @Size(min = 6, message = "USERNAME_INVALID")
    String username;
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    @DateOfBirthConstraint(min=18, message = "DOB_INVALID")
    LocalDate dateOfBirth;

}
