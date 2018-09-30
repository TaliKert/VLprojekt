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
    UserRepository userRepository;

    public User getUser(Principal principal) {
        Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        String googleId = (String)details.get("sub");
        User user = userRepository.findByGoogleId(googleId);
        if(user == null) {
            return null;
        }
        return user;
    }

    public boolean addUser(String username, Principal principal) {
        Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        if (userRepository.findByUsername(username) == null) {
            return false;
        }
        User user = new User();
        user.setRegistrationDate(LocalDate.now());
        user.setGoogleId((String)details.get("id"));
        user.setEmail((String)details.get("email"));
        user.setUsername(username);
        userRepository.save(user);
        return true;
    }
}
