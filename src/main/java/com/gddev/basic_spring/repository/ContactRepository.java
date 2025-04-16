package com.gddev.basic_spring.repository;

import com.gddev.basic_spring.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findAllByUserId(String userId);
}
