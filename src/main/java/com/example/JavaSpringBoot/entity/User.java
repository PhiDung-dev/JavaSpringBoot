package com.example.JavaSpringBoot.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
  String username;
  String password;
  String firstName;
  String lastName;
  String dateOfBirth;

  @ManyToMany Set<Role> roles;
}
