package com.springboot.airbnb.service;

import com.springboot.airbnb.dto.ProfileUpdateRequestDto;
import com.springboot.airbnb.entity.User;

public interface UserService {

    User findUserById(Long userId);

    Void updateUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto);
}
