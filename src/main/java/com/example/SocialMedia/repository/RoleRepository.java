package com.example.SocialMedia.repository;

import com.example.SocialMedia.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
   Role findByName(String roleName);
}
