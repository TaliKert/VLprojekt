package com.kmk.imageboard.controller;

import com.kmk.imageboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Observable;
import java.util.Observer;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String registerPrompt() {
        return "register";
    }

    @RequestMapping("/user")
    public @ResponseBody Principal user(Principal principal) {
        return principal;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody String username, Principal principal) {
        userService.addUser(username, principal);
        ResponseEntity result = new ResponseEntity(HttpStatus.OK);
        return result;
    }

}
