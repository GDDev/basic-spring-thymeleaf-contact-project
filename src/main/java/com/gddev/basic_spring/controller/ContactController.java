package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.ContactDTO;
import com.gddev.basic_spring.repository.UserRepository;
import com.gddev.basic_spring.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contato")
public class ContactController {

    @Autowired
    private ContactService service;
    @Autowired
    private UserRepository userRepository;

    /**
     * Fetches the current context to find and retrieve the logged user.
     *
     * @return User the logged-in User object found.
     */
    private User getLoggedUser() {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Fetches all the contacts of the logged user.
     *
     * @return List<Contact> List of found contacts.
     */
    @GetMapping("/listar")
    public ResponseEntity getAllContacts() {
        User user = getLoggedUser();
        List<Contact> contacts = service.findAllContacts(user.getId());
        return ResponseEntity.ok(contacts);
    }

    /**
     * Fetches a Contact by its ID.
     *
     * @param id String of the desired contact ID.
     * @return ResponseEntity with either the found contact or a notFound.
     */
    @GetMapping("/{id}")
    public ResponseEntity getContactById(@PathVariable String id) {
        Optional<Contact> contato = service.findContactById(id);
        if (contato.isPresent()) {
            User user = getLoggedUser();
            if (contato.get().getUser().getId().equals(user.getId())) {
                return ResponseEntity.ok(contato.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Registers a new Contact associated to the logged user.
     *
     * @param contact Contact DTO with the contact info.
     * @return ResponseEntity with the success of the registration.
     */
    @PostMapping("/novo")
    @Transactional
    public ResponseEntity saveContact(@RequestBody @Valid ContactDTO contact) {
        User user = getLoggedUser();
        service.saveContact(contact, user);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing Contact.
     *
     * @param contact Contact DTO with the info to be updated.
     * @param id String of the existing contact.
     * @return ResponseEntity.
     */
    @PutMapping("/atualizar/{id}")
    @Transactional
    public ResponseEntity updateContact(@RequestBody @Valid ContactDTO contact, @PathVariable String id) {
        Optional<Contact> optionalContact = this.service.findContactById(id);
        if (optionalContact.isPresent()) {
            User user = getLoggedUser();
            if (optionalContact.get().getUser().getId().equals(user.getId())) {
                Contact updatedContact = this.service.updateContact(id, contact);
                return ResponseEntity.ok(updatedContact);
            }
            return ResponseEntity.badRequest().body("Acesso negado.");
        }
        throw new EntityNotFoundException();
    }

    /**
     * Deletes an existing Contact.
     *
     * @param id String ID of the contact to be deleted.
     * @return ResponseEntity.
     */
    @DeleteMapping("/excluir/{id}")
    @Transactional
    public ResponseEntity deleteContact(@PathVariable String id) {
        Optional<Contact> optionalContact = this.service.findContactById(id);
        if (optionalContact.isPresent()) {
            User user = getLoggedUser();
            if (optionalContact.get().getUser().getId().equals(user.getId())) {
                this.service.deleteContact(optionalContact);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().body("Acesso negado.");
        }
        throw new EntityNotFoundException();
    }
}
