package com.tcs.training.service;

import com.tcs.training.bean.Signup;
import com.tcs.training.repository.SignupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
Spring will automatically use MyUserDetailsService class when we write:
    - auth.userDetailsService(userDetailsService);
*/

@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired //Injects the SignupRepo object/bean (automatically created by Spring), which object/bean is already created by the Spring IOC early before this injections
    SignupRepo signupRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Signup> signup = signupRepo.findByUsername(username);

        if(signup.isPresent()){
            return new MyUserDetails(signup.get()); //We can't directly return object of UserDetails, since UserDetails is an interface.
        }
        else{
            throw new UsernameNotFoundException("User Not Found..");
        }
        /*
        ✅ If user is found:
            - It returns a new instance of MyUserDetails, a custom class that implements UserDetails.
            - This object holds user’s username, password, roles, etc., and Spring uses it for authentication.

        ❌ If user is not found, an exception is thrown → Spring Security rejects login and shows an error.
        */
    }
}