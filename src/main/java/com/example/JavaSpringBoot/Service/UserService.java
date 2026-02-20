package com.example.JavaSpringBoot.Service;

import com.example.JavaSpringBoot.Entity.User;
import com.example.JavaSpringBoot.Repository.UserRepository;
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
        User user = readUser(id);
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
