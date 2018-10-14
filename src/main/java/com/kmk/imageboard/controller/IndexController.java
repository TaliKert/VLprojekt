package com.kmk.imageboard.controller;

import com.kmk.imageboard.service.UserService;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class IndexController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        return "index";
    }

    @GetMapping("/register")
    public String registerPrompt(Principal principal) {
        if (userService.getUser(principal) != null) {
            return "redirect:/";
        }
        return "register";
    }

    @GetMapping("/map")
    public String map(Model model, Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        return "map";
    }

    @GetMapping("/statistics")
    public String statistics(Model model, Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        return "statistics";
    }

    @GetMapping("/upload")
    public String upload(Model model, Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        return "upload";
    }

    @RequestMapping("/user")
    public @ResponseBody
    Principal user(Principal principal) {
        return principal;
    }

}
