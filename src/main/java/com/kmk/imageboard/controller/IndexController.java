package com.kmk.imageboard.controller;

import com.kmk.imageboard.service.ImageService;
import com.kmk.imageboard.service.UserService;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.rsa.RSAPadding;
import sun.security.ssl.RSASignature;

import java.security.Principal;
import java.security.Signature;

@Controller
public class IndexController {

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

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

    @GetMapping("/entry/{id}")
    public String indexWithSpecifiedEntry(Model model, Principal principal, @PathVariable String id) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        if (!imageService.imageExists(Long.parseLong(id))) return "entrynotfound";
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

    @GetMapping("/sitemap")
    public String sitemap(Model model, Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        return "sitemap";
    }

    @GetMapping("/u/{username}")
    public String user(Model model, Principal principal, @PathVariable String username) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        if (userService.getUser(username) != null) {
            model.addAttribute("whosePage", username);
            model.addAttribute("uploadCount", imageService.getUserUploadCount(username));
        }
        return "user";
    }

    @GetMapping("/donate")
    public String donate(Model model, Principal principal) {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        return null;
    }
}
