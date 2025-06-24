package com.tcs.training.controller;

import com.tcs.training.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tcs.training.bean.AuthRequest;
import com.tcs.training.util.*;

@RestController
public class AuthController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //    1. First hit this from the client
//    @PostMapping("/authenticate")
//    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
//        if ("admin".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
//            return jwtUtil.generateToken(authRequest.getUsername());
//        } else {
//            throw new Exception("Invalid credentials");
//        }
//    }


    @PostMapping("/authenticate")
    public String createAuthToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return jwt;
    }


    //    2. Once authenticated, this should be accessible
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome! This is a secured endpoint.";
    }
}