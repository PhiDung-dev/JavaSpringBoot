package com.example.JavaSpringBoot.dto.request;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String password, String firstName, String lastName, String dateOfBirth) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
