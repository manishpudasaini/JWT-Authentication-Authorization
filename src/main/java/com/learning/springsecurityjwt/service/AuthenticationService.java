package com.learning.springsecurityjwt.service;

import com.learning.springsecurityjwt.auth.AuthenticationRequest;
import com.learning.springsecurityjwt.auth.AuthenticationResponse;
import com.learning.springsecurityjwt.auth.RegisterRequest;
import com.learning.springsecurityjwt.config.JwtService;
import com.learning.springsecurityjwt.user.Role;
import com.learning.springsecurityjwt.user.User;
import com.learning.springsecurityjwt.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; //it helps to authenticate user on the basis of username & password

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user); //saving the user data in database
        var jwtToken = jwtService.generateToken(user); //calling jwt service which create a token

        return AuthenticationResponse.builder()  //returning the token created using user details
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword())
        );
        //if the user is authenticate
        var user  = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user); //calling jwt service which create a token

        return AuthenticationResponse.builder()  //returning the token created using user details
                .token(jwtToken)
                .build();

    }
}
