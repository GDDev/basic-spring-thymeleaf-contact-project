package com.gddev.basic_spring.repository;

import com.gddev.basic_spring.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, String> {
    /**
     * Finds all the contacts associated with a user.
     *
     * @param userId String of the User's ID.
     * @return List<Contact> List of all the contacts found.
     */
    List<Contact> findAllByUserId(String userId);
}
