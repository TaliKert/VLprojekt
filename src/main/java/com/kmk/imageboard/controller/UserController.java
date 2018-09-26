package com.kmk.imageboard.controller;

import com.kmk.imageboard.model.User;
import com.kmk.imageboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping(path = "/")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/register")
    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        n.setRegistrationDate(LocalDate.now());

        userRepository.save(n);
        return "Saved";
    }

    @GetMapping("/user/me")
    public Principal user(Principal principal) {
        return principal;
    }
}
