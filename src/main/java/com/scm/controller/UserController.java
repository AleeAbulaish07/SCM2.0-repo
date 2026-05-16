package com.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // User dashboard page
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String userDashboard() {

        return "user/dashboard";
    }

    // User profile page
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfile(Model model, Authentication authentication) {

        // Logged in user email / username
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in: {}", username);

        // Database se user fetch karo
        User user = userService.getUserByEmail(username);

        // Agar user null hai
        if (user == null) {

            logger.info("User not found in database");

            return "redirect:/login";
        }

        // Console check
        System.out.println(user.getName());
        System.out.println(user.getEmail());

        // Model me bhejo
        model.addAttribute("loggedInUser", user);

        return "user/profile";
    }
}