package com.kmk.imageboard.controller;

import com.kmk.imageboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class IndexController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
        }
        return "index";
    }

}
