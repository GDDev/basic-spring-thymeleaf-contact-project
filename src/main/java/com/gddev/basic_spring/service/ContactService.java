package com.gddev.basic_spring.service;

import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.ContactDTO;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.repository.ContactRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    ContactRepository repo;

    public void saveContact(ContactDTO contact, User user) {
        Contact newContact = Contact.builder()
                .name(contact.name())
                .nickname(contact.nickname())
                .email(contact.email())
                .phone(contact.phone())
                .address(contact.address())
                .user(user)
                .build();
        repo.save(newContact);
    }

    public Contact updateContact(ContactDTO contact, User user) {
        Contact updatedContact = Contact.builder()
                .name(contact.name())
                .nickname(contact.nickname())
                .email(contact.email())
                .phone(contact.phone())
                .address(contact.address())
                .user(user)
                .build();
        return repo.save(updatedContact);
    }

    public void deleteContact(Optional<Contact> contact) {
        contact.ifPresent(value -> repo.delete(value));
    }

    public Optional<Contact> findContactById(UUID id) {
        return repo.findById(id);
    }

    public List<Contact> findAllContacts() {
        return repo.findAll();
    }
}
