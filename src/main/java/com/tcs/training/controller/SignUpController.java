package com.tcs.training.controller;

import com.tcs.training.bean.AuthRequest;
import com.tcs.training.bean.Signup;
import com.tcs.training.repository.SignupRepo;
import com.tcs.training.service.MyUserDetailsService;
import com.tcs.training.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class SignUpController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //    1. First hit this from the client
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


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome! This is a public URL.";
    }

    @Autowired
    SignupRepo signupRepo;

    //    2. Once authenticated, this should be accessible
    @GetMapping("/admin/users") //For "ADMIN"
    public List<Signup> getAllUser(){
        return signupRepo.findAll();
    }

    @GetMapping("/user-data") //For "USER"
    public Optional<Signup> getUserById(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        String username = usernamePasswordAuthenticationToken.getName();
        return signupRepo.findByUsername(username);
    }


}
