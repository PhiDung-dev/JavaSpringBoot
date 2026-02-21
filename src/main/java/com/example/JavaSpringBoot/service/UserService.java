package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.repository.UserRepository;
import com.example.JavaSpringBoot.dto.request.UserCreateRequest;
import com.example.JavaSpringBoot.dto.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(UserCreateRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        return userRepository.save(user);
    }

    public List<User> readUsers() {
        return userRepository.findAll();
    }

    public User readUser(String id) {
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found"));
    }

    public User updateUser(String id, UserUpdateRequest request) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("user not found");
        }
        User user = readUser(id);
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("user not found");
        }
        userRepository.deleteById(id);
    }

}
