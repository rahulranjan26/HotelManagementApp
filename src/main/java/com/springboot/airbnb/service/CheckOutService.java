package com.springboot.airbnb.service;

import com.springboot.airbnb.entity.Booking;

public interface CheckOutService {
    String getCheckOutSession(Booking booking, String successUrl, String failureUrl);
}
