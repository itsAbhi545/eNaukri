package com.chicmic.eNaukri.service;

import com.chicmic.eNaukri.CustomExceptions.ApiException;
import com.chicmic.eNaukri.model.Premium;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.PremiumRepo;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Product;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private  final UsersRepo usersRepo;
    private final PremiumRepo premiumRepo;

    public String startSubscription(Long userId) throws StripeException {

        Users user=usersRepo.findById(userId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND,"User doesn't exist"));
        Premium premium=new Premium();
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams
                .Builder()
                .setCurrency("INR")
                .setAmount(250* 100L)
                .build();
        PaymentIntent intent = PaymentIntent.create(createParams);
        PaymentIntent confirmIntent = intent.confirm();
        // Check if the payment is successful
        if ("succeeded".equals(confirmIntent.getStatus())) {
            user.setHasPremium(true);
            premium.setUserSubscription(user);
            premiumRepo.save(premium);
            return intent.getInvoice();
        }
        return null;
    }
}
