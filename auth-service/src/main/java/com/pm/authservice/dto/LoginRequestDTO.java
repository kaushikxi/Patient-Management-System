package com.pm.authservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {

    @NotNull(message="Email Address is required")
    @Email(message = "Email Address must be valid")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min=8, message = "Password must be at least 8 characters")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
