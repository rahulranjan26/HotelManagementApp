package com.springboot.airbnb.service.Impl;


import com.springboot.airbnb.dto.ProfileUpdateRequestDto;
import com.springboot.airbnb.entity.User;
import com.springboot.airbnb.exceptions.ResourceNotFoundException;
import com.springboot.airbnb.repository.UserRepository;
import com.springboot.airbnb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found!!" + userId)
        );
    }

    @Override
    public Void updateUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (profileUpdateRequestDto.getName() != null)
            user.setName(profileUpdateRequestDto.getName());
        if (profileUpdateRequestDto.getGender() != null)
            user.setGender(profileUpdateRequestDto.getGender());
        if (profileUpdateRequestDto.getDateOfBirth() != null)
            user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());

        userRepository.save(user);
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}
