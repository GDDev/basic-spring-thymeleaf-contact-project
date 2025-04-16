package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.service.ContactService;
import com.gddev.basic_spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("layout");
        String fragment = "home :: content";
        log.info("Current fragment: " + fragment);
        mav.addObject("content", fragment);
        return mav;
    }

    @GetMapping("/register")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/contatos")
    public String contactList(Model model) {
        List<Contact> contacts = contactService.findAllContacts();
        model.addAttribute("contacts", contacts);
        String fragment = "contacts :: content";
        log.info("Current fragment: " + fragment);
        model.addAttribute("content", fragment);
        return "contacts";
    }

    @GetMapping("/contatos/novo")
    public ModelAndView newContact() {
        ModelAndView mav = new ModelAndView("layout");
        mav.addObject("content", "newContact :: content");
        return mav;
    }

    @GetMapping("/error")
    public String error() { return "error"; }
}
