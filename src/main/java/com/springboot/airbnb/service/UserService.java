package com.springboot.airbnb.service;

import com.springboot.airbnb.entity.User;

public interface UserService {

    User findUserById(Long userId);
}
