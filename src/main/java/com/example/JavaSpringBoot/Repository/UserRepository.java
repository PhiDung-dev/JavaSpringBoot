package com.example.JavaSpringBoot.Repository;

import com.example.JavaSpringBoot.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
