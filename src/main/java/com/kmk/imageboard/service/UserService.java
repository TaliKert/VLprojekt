package com.kmk.imageboard.service;

import com.kmk.imageboard.model.User;
import com.kmk.imageboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    public User getUser(Principal principal) {
        Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        String googleId = (String)details.get("id");
        User user = userRepository.findByGoogleId(googleId);
        return user;
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUser(long id) {
        return userRepository.findById(id);
    }

    public boolean addUser(String username, Principal principal) {
        Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        userRepository.save(username, (String)details.get("email"), LocalDate.now(), (String)details.get("id"));
        emailService.sendWelcomeEmail((String)details.get("email"), username);
        return true;
    }
}
