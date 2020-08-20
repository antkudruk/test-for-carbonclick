package com.carbonclick.tsttask.secretsanta.user.controller;

import com.carbonclick.tsttask.secretsanta.user.controller.request.LoginRequest;
import com.carbonclick.tsttask.secretsanta.user.controller.response.LoginResponse;
import com.carbonclick.tsttask.secretsanta.user.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping
    @ResponseBody
    public LoginResponse getToken(@RequestBody LoginRequest user, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword());

        authenticationManager.authenticate(authToken);
        return LoginResponse
                .builder()
                .token(tokenProvider.createToken(user.getUsername()))
                .build();
    }
}