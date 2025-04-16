package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.repository.UserRepository;
import com.gddev.basic_spring.service.ContactService;
import com.gddev.basic_spring.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactService contactService;

//    @GetMapping("/")
//    public ModelAndView home() {
//        ModelAndView mav = new ModelAndView("layout");
//        String fragment = "home :: content";
//        log.info("Current fragment: " + fragment);
//        mav.addObject("content", fragment);
//        return mav;
//    }

    @GetMapping("/register")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/")
    public String contactList(Model model) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);

        List<Contact> contacts = contactService.findAllContacts(user.getId());
        model.addAttribute("contacts", contacts);
        String fragment = "contacts :: content";
        log.info("Current fragment: " + fragment);
        model.addAttribute("content", fragment);
        return "contacts";
    }

    @GetMapping("/novoContato")
    public String newContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "newContact";
    }

    @GetMapping("/contato/atualizar/{id}")
    public String editContact(Model model, @PathVariable String id) {
        Contact contact = contactService.findContactById(id).orElseThrow(EntityNotFoundException::new);
        if (contact != null) {
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findById(loggedUser.getId()).orElseThrow(EntityNotFoundException::new);
            if (user.getId().equals(contact.getUser().getId())) {
                model.addAttribute("contact", contact);
                return "updateContact";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/error")
    public String error() { return "error"; }
}
