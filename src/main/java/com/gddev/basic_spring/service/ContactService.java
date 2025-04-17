package com.gddev.basic_spring.service;

import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.ContactDTO;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.repository.ContactRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    ContactRepository repo;

    /**
     * Instantiates a Contact class and saves it into the database.
     *
     * @param contact Contact DTO with the contact info.
     * @param user User to associate the contact with.
     */
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

    /**
     * Instantiates an object of the contact, updates it and saves into the database.
     *
     * @param contactId String of the ID of the contact to be updated.
     * @param updatedContact Contact DTO with the updated contact info.
     * @return Updated contact object.
     */
    public Contact updateContact(String contactId, ContactDTO updatedContact) {
        Contact contact = repo.findById(contactId).orElseThrow(EntityNotFoundException::new);
        contact.setName(updatedContact.name());
        contact.setNickname(updatedContact.nickname());
        contact.setEmail(updatedContact.email());
        contact.setPhone(updatedContact.phone());
        contact.setAddress(updatedContact.address());
        return repo.save(contact);
    }

    /**
     * Deletes a contact.
     *
     * @param contact Contact to be deleted.
     */
    public void deleteContact(Optional<Contact> contact) {
        contact.ifPresent(value -> repo.delete(value));
    }

    /**
     * Finds a contact by its ID.
     *
     * @param id String ID of the contact.
     * @return Optional<Contact>, if exists returns the Contact object.
     */
    public Optional<Contact> findContactById(String id) {
        return repo.findById(id);
    }

    /**
     * Finds all the contacts associated with a user ID.
     *
     * @param userId String ID of the user.
     * @return List<Contact> list of contacts found belonging to the user.
     */
    public List<Contact> findAllContacts(String userId) {
        return repo.findAllByUserId(userId);
    }
}
