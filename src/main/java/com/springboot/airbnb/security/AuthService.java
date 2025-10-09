package com.springboot.airbnb.security;


import com.springboot.airbnb.dto.LoginDto;
import com.springboot.airbnb.dto.SignUpDTO;
import com.springboot.airbnb.dto.UserDto;
import com.springboot.airbnb.entity.User;
import com.springboot.airbnb.entity.enums.Role;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public UserDto signUp(SignUpDTO signUpDTO) {
        User user = userRepository.findByEmail(signUpDTO.getEmail()).orElse(null);
        if (user != null) {
            throw new IllegalArgumentException("The user is already present with the email " + signUpDTO.getEmail());
        }
        User newUser = User.builder()
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .name(signUpDTO.getName())
                .roles(Set.of(Role.GUEST))
                .build();
        User savedUser = userRepository.save(newUser);
        return UserDto.builder()
                .email(signUpDTO.getEmail())
                .name(signUpDTO.getName())
                .roles(savedUser.getRoles())
                .userId(savedUser.getUserId())
                .build();
    }

    public String[] login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        User user = (User) authentication.getPrincipal();

        String[] tokens = new String[2];
        tokens[0] = jwtService.generateAccessToken(user);
        tokens[1] = jwtService.getRefreshToken(user);
        return tokens;

    }

    public String refresh(String refreshToken) {
        Long userId = jwtService.getUserId(refreshToken);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User doesnt exist"));

        return jwtService.generateAccessToken(user);

    }


}
