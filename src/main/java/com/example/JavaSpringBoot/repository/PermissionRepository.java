package com.example.JavaSpringBoot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.JavaSpringBoot.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
