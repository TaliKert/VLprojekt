package com.kmk.imageboard.controller.api;

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
    @ResponseBody
    public ResponseEntity registerUser(@ModelAttribute("username") String username, Principal principal) {
        if (userService.getUser(username) != null || userService.getUser(principal) != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        userService.addUser(username, principal);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
