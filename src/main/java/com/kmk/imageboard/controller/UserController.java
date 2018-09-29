package com.kmk.imageboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Observable;
import java.util.Observer;

@Controller
public class UserController {

    @RequestMapping("/user")
    public @ResponseBody Principal user(Principal principal) {
        return principal;
    }

}
