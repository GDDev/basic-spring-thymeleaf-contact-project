package com.gddev.basic_spring.service;

import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.ContactDTO;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.model.UserDTO;
import com.gddev.basic_spring.repository.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Contact updateContact(String contactId, ContactDTO updatedContact, User user) {
        Contact contact = repo.findById(contactId).orElseThrow(EntityNotFoundException::new);
        contact.setName(updatedContact.name());
        contact.setNickname(updatedContact.nickname());
        contact.setEmail(updatedContact.email());
        contact.setPhone(updatedContact.phone());
        contact.setAddress(updatedContact.address());
        return repo.save(contact);
    }

    public void deleteContact(Optional<Contact> contact) {
        contact.ifPresent(value -> repo.delete(value));
    }

    public Optional<Contact> findContactById(String id) {
        return repo.findById(id);
    }

    public List<Contact> findAllContacts(String userId) {
        return repo.findAllByUserId(userId);
    }
}
