package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.ContactDTO;
import com.gddev.basic_spring.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/contato")
public class ContactController {

    @Autowired
    private ContactService service;

    @GetMapping("/listar")
    public ResponseEntity getAllContacts() {
        List<Contact> contacts = service.findAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity getContactById(@PathVariable UUID id) {
        Optional<Contact> contato = service.findContactById(id);
        if (contato.isPresent()) {
            return ResponseEntity.ok(contato.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/novo")
    public ResponseEntity saveContact(@RequestBody @Valid ContactDTO contact) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) auth.getPrincipal();

        service.saveContact(contact, loggedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/atualizar/{id}")
    @Transactional
    public ResponseEntity updateContact(@RequestBody @Valid ContactDTO contact, @PathVariable UUID id) {
        Optional<Contact> optionalContact = this.service.findContactById(id);
        if (optionalContact.isPresent()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User loggedUser = (User) auth.getPrincipal();

            Contact updatedContact = this.service.updateContact(contact, loggedUser);
            return ResponseEntity.ok(updatedContact);
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping("/excluir/{id}")
    @Transactional
    public ResponseEntity deleteContact(@PathVariable UUID id) {
        Optional<Contact> optionalContact = this.service.findContactById(id);
        if (optionalContact.isPresent()) {
            this.service.deleteContact(optionalContact);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
}
