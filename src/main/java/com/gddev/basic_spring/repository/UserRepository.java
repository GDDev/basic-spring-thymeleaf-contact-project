package com.gddev.basic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gddev.basic_spring.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByUsername(String username);

}
