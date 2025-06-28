package com.tcs.training.controller;

import com.tcs.training.bean.Signup;
import com.tcs.training.repository.SignupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class SignUpController {

    @Autowired
    SignupRepo signupRepo;

    @GetMapping("/allUser")
    public List<Signup> getAllUser(){
        return signupRepo.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<Signup> getUserById(@PathVariable Long id, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
        String username = usernamePasswordAuthenticationToken.getName();
        Optional<Signup> signup = signupRepo.findByUsername(username);

        if(!(signup.get().getId()==id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Not Found..");
        }
        return signup;
    }


}
