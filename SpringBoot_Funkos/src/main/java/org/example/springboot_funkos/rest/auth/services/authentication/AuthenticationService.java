package org.example.springboot_funkos.rest.auth.services.authentication;


import jakarta.validation.Valid;
import org.example.springboot_funkos.rest.auth.dto.JwtAuthResponse;

public interface AuthenticationService {
    JwtAuthResponse signUp(org.example.springboot_funkos.rest.auth.dto.@Valid UserSignUpRequest request);

    JwtAuthResponse signIn(org.example.springboot_funkos.rest.auth.dto.@Valid UserSignInRequest request);
}