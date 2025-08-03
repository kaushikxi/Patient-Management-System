package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // password submitted by the user is encoded and is compared with the encoded password in the DB
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO){
        Optional<String> token = userService.
                findByEmail(loginRequestDTO.getEmail()) // we're going to get the user from email
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
                        u.getPassword())) // u is user, passwordEncoder checks whether the password submitted by the user matches to the password from the db; if matches-> next method;if not then null and won't go to map method
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole())); // map method replaces the User object with a token

        return token;
    }

    public boolean validateToken(String token){
        try{
            jwtUtil.validateToken(token);
            return true;
        } catch(JwtException e){
            return false;
        }
    }
}
