package com.springboot.airbnb.strategy;

import com.springboot.airbnb.entity.Inventory;


import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
