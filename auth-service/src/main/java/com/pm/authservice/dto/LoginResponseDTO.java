package com.pm.authservice.dto;

public class LoginResponseDTO {

    private final String token; // once initialized, cannot be re initialized again- cannot overwrite the token

    public LoginResponseDTO(String token){ // it is better to use a constructor instead of a setter since it's a cleaner approach as we can initialize the object with less code
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
