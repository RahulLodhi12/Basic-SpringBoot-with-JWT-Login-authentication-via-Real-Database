package com.tcs.training.repository;

import com.tcs.training.bean.Signup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignupRepo extends JpaRepository<Signup,Long> {

    Optional<Signup> findByUsername(String username);
}