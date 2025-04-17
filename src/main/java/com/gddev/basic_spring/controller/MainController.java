package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.Contact;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.repository.UserRepository;
import com.gddev.basic_spring.service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

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

    /**
     * Renders the sign-up page.
     *
     * @param model Model.
     * @return signup HTML page.
     */
    @GetMapping("/register")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Renders the log-in page.
     *
     * @return login HTML page.
     */
    @GetMapping("/login")
    public String login() { return "login"; }

    /**
     * Renders the contact listing page.
     *
     * @param model Model.
     * @return contacts HTML page.
     */
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

    /**
     * Renders the new contact page.
     *
     * @param model Model.
     * @return newContact HTML page.
     */
    @GetMapping("/novoContato")
    public String newContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "newContact";
    }

    /**
     * Renders the contact update page.
     *
     * @param model Model.
     * @param id String of the Contact to be updated.
     * @return / home page (contact listing).
     */
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

    /**
     * Renders customized error page.
     *
     * @return error HTML page.
     */
    @GetMapping("/error")
    public String error() { return "error"; }
}
