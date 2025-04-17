package com.gddev.basic_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gddev.basic_spring.model.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by their username.
     *
     * @param username String of the username used by the user.
     * @return UserDetails Spring Security's User Model
     */
    UserDetails findByUsername(String username);
}
