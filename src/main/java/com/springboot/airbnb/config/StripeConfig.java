package com.springboot.airbnb.config;


import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    public StripeConfig(@Value("${stripe.secret_key}") String stripeSecretKey){
        Stripe.apiKey = stripeSecretKey;
    }
}
