package com.example.JavaSpringBoot.configuration;

import java.util.HashSet;
import java.util.Set;

import com.example.JavaSpringBoot.entity.Role;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;

  @Bean
  @ConditionalOnProperty(
      prefix = "spring",
      value = "datasource.driverClassName",
      havingValue = "com.mysql.cj.jdbc.Driver")
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    return args -> {
      if (!userRepository.existsByUsername("admin")) {
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ADMIN").orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
                        roles.add(role);
        User user =
            User.builder()
                .username("admin")
                    .roles(roles)
                .password(passwordEncoder.encode("admin"))
                .build();
        userRepository.save(user);
        log.warn("admin user has been create with default password: admin, please change it");
      }
    };
  }
}
