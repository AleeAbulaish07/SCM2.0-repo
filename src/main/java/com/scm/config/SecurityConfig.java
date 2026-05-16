package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    private final SecurityCustomUserDetailService userDetailsService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    public SecurityConfig(SecurityCustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // authentication provider

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    // security filter chain

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authenticationProvider(authenticationProvider());

        httpSecurity.authorizeHttpRequests(authorize -> {

            // protected routes

            authorize.requestMatchers("/user/**").authenticated();

            // public routes

            authorize.requestMatchers(
                    "/",
                    "/login",
                    "/register",
                    "/do-register",
                    "/css/**",
                    "/js/**",
                    "/images/**"
            ).permitAll();

            authorize.anyRequest().permitAll();

        });

        // login config

        httpSecurity.formLogin(formLogin -> {

            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile", true);

            formLogin.failureUrl("/login?error=true");

            formLogin.usernameParameter("email");

            formLogin.passwordParameter("password");

            formLogin.permitAll();

        });

        // logout config
        
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");

           // logoutForm.permitAll();

        });

        // oauth2 login config

        httpSecurity.oauth2Login(oauth2Login -> {

            oauth2Login.loginPage("/login");
            oauth2Login.successHandler(handler);

           // oauth2Login.defaultSuccessUrl("/user/dashboard", true);

        });

        return httpSecurity.build();
    }

    // password encoder

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

}