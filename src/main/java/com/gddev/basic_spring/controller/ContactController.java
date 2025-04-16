package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.ContactDTO;
import com.gddev.basic_spring.model.UserDTO;
import com.gddev.basic_spring.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    private User getLoggedUser() {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping("/listar")
    public ResponseEntity getAllContacts() {
        User user = getLoggedUser();
        List<Contact> contacts = service.findAllContacts(user.getId());
        return ResponseEntity.ok(contacts);
    }

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

    @PostMapping("/novo")
    @Transactional
    public ResponseEntity saveContact(@RequestBody @Valid ContactDTO contact) {
        User user = getLoggedUser();
        service.saveContact(contact, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/atualizar/{id}")
    @Transactional
    public ResponseEntity updateContact(@RequestBody @Valid ContactDTO contact, @PathVariable String id) {
        Optional<Contact> optionalContact = this.service.findContactById(id);
        if (optionalContact.isPresent()) {
            User user = getLoggedUser();
            if (optionalContact.get().getUser().getId().equals(user.getId())) {
                Contact updatedContact = this.service.updateContact(id, contact,user);
                return ResponseEntity.ok(updatedContact);
            }
            return ResponseEntity.badRequest().body("Acesso negado.");
        }
        throw new EntityNotFoundException();
    }

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
