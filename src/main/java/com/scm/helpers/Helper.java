package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        // OAuth login
        if (principal instanceof OAuth2User) {

            OAuth2AuthenticationToken oauth2AuthenticationToken =
                    (OAuth2AuthenticationToken) authentication;

            String clientId =
                    oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            OAuth2User oauth2User = (OAuth2User) principal;

            // Google login
            if (clientId.equalsIgnoreCase("google")) {

                System.out.println("Getting email from google");

                return oauth2User.getAttribute("email").toString();
            }

            // GitHub login
            else if (clientId.equalsIgnoreCase("github")) {

            System.out.println("Getting email from github");

            String email = oauth2User.getAttribute("email");

            if (email == null) {

            return oauth2User.getAttribute("login").toString() + "@gmail.com";
            }

            return email;
        }
        }

        // Local login
        return authentication.getName();
    }
}