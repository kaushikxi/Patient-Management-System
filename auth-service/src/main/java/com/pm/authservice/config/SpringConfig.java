package com.pm.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {  // configures Spring Security to be less secure than it needs to be
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()) // tells spring to authorize any request it receives to the AuthService without any security checks
                .csrf(AbstractHttpConfigurer::disable);// disable the cross site request forgery; we don't need this since our traffic is coming from api gateway

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // encodes the password
    }
}
