package com.learning.springsecurityjwt.user.repo;

import com.learning.springsecurityjwt.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
