package com.kmk.imageboard.controller;

import com.kmk.imageboard.service.BankService;
import com.kmk.imageboard.service.ImageService;
import com.kmk.imageboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.PublicKey;

@Controller
public class IndexController {

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

    @Autowired
    BankService bankService;

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
    public String donate(Model model, Principal principal) throws Exception {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        model.addAttribute("VK_MAC", bankService.sign(bankService.generateServerSideData(), bankService.privateKeyFromResources()));
        model.addAttribute("VK_SERVICE", bankService.getVK_SERVICE());
        model.addAttribute("VK_VERSION", bankService.getVK_VERSION());
        model.addAttribute("VK_SND_ID", bankService.getVK_SND_ID());
        model.addAttribute("VK_STAMP", bankService.getVK_STAMP());
        model.addAttribute("VK_AMOUNT", bankService.getVK_AMOUNT());
        model.addAttribute("VK_CURR", bankService.getVK_CURR());
        model.addAttribute("VK_ACC", bankService.getVK_ACC());
        model.addAttribute("VK_NAME", bankService.getVK_NAME());
        model.addAttribute("VK_REF", bankService.getVK_REF());
        model.addAttribute("VK_LANG", bankService.getVK_LANG());
        model.addAttribute("VK_MSG", bankService.getVK_MSG());
        model.addAttribute("VK_RETURN", bankService.getVK_RETURN());
        model.addAttribute("VK_CANCEL", bankService.getVK_CANCEL());
        model.addAttribute("VK_DATETIME", bankService.getVK_DATETIME());
        model.addAttribute("VK_ENCODING", bankService.getVK_ENCODING());

        return "donate";
    }

    @PostMapping(value = "/donate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String donateRedir(Model model, Principal principal,
                              @RequestParam(value = "payment_action") String action,
                              @RequestBody MultiValueMap<String, String> formData) throws Exception {
        if (principal != null) {
            if (userService.getUser(principal) == null) {
                return "redirect:/register";
            }
            model.addAttribute("username", userService.getUser(principal).getUsername());
        }
        if (action.equals("success")) {
            PublicKey publicKey = bankService.publicKeyFromCertificate();
            String encodedResponse = bankService.generateData(
                    formData.getFirst("VK_SERVICE"),
                    formData.getFirst("VK_VERSION"),
                    formData.getFirst("VK_SND_ID"),
                    formData.getFirst("VK_REC_ID"),
                    formData.getFirst("VK_STAMP"),
                    formData.getFirst("VK_T_NO"),
                    formData.getFirst("VK_AMOUNT"),
                    formData.getFirst("VK_CURR"),
                    formData.getFirst("VK_REC_ACC"),
                    formData.getFirst("VK_REC_NAME"),
                    formData.getFirst("VK_SND_ACC"),
                    formData.getFirst("VK_SND_NAME"),
                    formData.getFirst("VK_REF"),
                    formData.getFirst("VK_MSG"),
                    formData.getFirst("VK_T_DATETIME")
            );
            bankService.verifyResponse(publicKey, formData.getFirst("VK_MAC"), encodedResponse);
            model.addAttribute("isPaymentConfirmed", true);
        } else if (action.equals("cancel")) {
            model.addAttribute("isPaymentCancelled", true);
        }
        return "donate";
    }
}
