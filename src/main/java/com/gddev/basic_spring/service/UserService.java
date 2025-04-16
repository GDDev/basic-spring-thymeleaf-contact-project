package com.gddev.basic_spring.service;

import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username);
    }

    public void newUser(User user) {
        if (this.repo.findByUsername(user.getUsername()) != null){ throw new RuntimeException("Usuário já existe."); }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }
}
