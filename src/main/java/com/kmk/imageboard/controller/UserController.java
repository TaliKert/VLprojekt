package com.kmk.imageboard.controller;

import com.kmk.imageboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody String username, Principal principal) {
        userService.addUser(username, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

}
