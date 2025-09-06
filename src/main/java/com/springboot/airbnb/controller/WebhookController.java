package com.springboot.airbnb.controller;

import com.springboot.airbnb.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook_secret}")
    private String endPointSecret;


    @PostMapping(path="/payments")
    public ResponseEntity<Void> capturePayments(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
        try{
            Event event = Webhook.constructEvent(payload,sigHeader,endPointSecret);
            bookingService.capturePayment(event);
            return ResponseEntity.noContent().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }


}
