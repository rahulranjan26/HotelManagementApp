package com.springboot.airbnb.controller;

import com.springboot.airbnb.dto.LoginDto;
import com.springboot.airbnb.dto.LoginResponseDTO;
import com.springboot.airbnb.dto.SignUpDTO;
import com.springboot.airbnb.dto.UserDto;
import com.springboot.airbnb.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDTO signUpDTO) {
        return new ResponseEntity<>(authService.signUp(signUpDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        String[] tokens = authService.login(loginDto);
        LoginResponseDTO responseDto = LoginResponseDTO.builder().accessToken(tokens[0]).build();

        Cookie cookie = new Cookie("refreshToken", tokens[1]);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response) {
        List<Cookie> allCookiesFromRequest = Arrays.stream(request.getCookies()).toList();
        for (var cookie : allCookiesFromRequest) {
            if (cookie.getName().equals("refreshToken")) {
                String refreshToken = cookie.getValue(); // ✅ Fixed!
                String newAccessToken = authService.refresh(refreshToken);
                return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Refresh token not found", HttpStatus.UNAUTHORIZED);
    }

}
