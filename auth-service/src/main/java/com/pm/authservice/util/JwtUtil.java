package com.pm.authservice.util;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component // registers this class as a Spring Bean and spring knows which class to autowire using Dependency injection
public class JwtUtil {

    private final Key secretKey; // what makes token secure is that they can only be verified with a secretKey; secretKey is a string of random characters stored securely on the server

    public JwtUtil(@Value("${jwt.secret}") String secret){ // we want secret to be as an env variable as we don't want anyone to access the secret
        byte[] keyBytes = Base64.getDecoder().
                decode(secret.getBytes(StandardCharsets.UTF_8)); // convert secret to byte Array

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role){
        return Jwts.builder()
                .subject(email) // subject is a standard field that is mainly used to store the ID that is going to relate the person
                .claim("role", role) // claim is a custom property that we can add to our Jwt
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600*10000)) // 10 hours
                .signWith(secretKey) // encode the token with a secret key
                .compact(); // squash all the properties into one single string
    }

    public void validateToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) secretKey) // parses the token and verifies the signature with the secret key from env
                    .build()
                    .parseSignedClaims(token);
        } catch(SignatureException e){
            throw new JwtException("Invalid JWT signature");
        } catch(JwtException e){
            throw new JwtException("Invalid JWT");
        }

    }



}
