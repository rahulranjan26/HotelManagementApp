package com.springboot.airbnb.strategy;

import com.springboot.airbnb.entity.Inventory;

import java.math.BigDecimal;


public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
