package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.service.SubscriptionService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
//import com.stripe.model.billingportal.Session;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import com.stripe.param.PriceListParams;
//import com.stripe.param.billingportal.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.net.ApiResource;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.Price;
import com.stripe.param.PriceListParams;
import com.stripe.model.PriceCollection;
import com.stripe.model.Discount;
import com.stripe.model.Subscription;
import spark.Request;
import spark.Response;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {
//    @Value("stripe.api.key")
//    private String apiKey;
    private Logger logger= LoggerFactory.getLogger(SubscriptionController.class);
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    private final SubscriptionService service;
    @PostMapping("/user/{userId}/buy-premium")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> subscribe(@PathVariable Long userId)throws StripeException {
        service.startSubscription(userId);
        return ResponseEntity.ok("You are now subscribed to eNaukri premium!");
    }
    @GetMapping("getProduct")
    public String subPlan() throws StripeException{
        Product product =
                Product.retrieve("prod_O1xZZ9DLUqVrZG");
        return product.getDefaultPrice();
    }
    @GetMapping("/v1/prices")
    public String prices() throws StripeException{
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 3);
        PriceCollection prices = Price.list(params);
        return prices.toString();
    }
    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckout(@RequestParam("lookup_key") String lookup_key, HttpServletRequest request, HttpServletResponse response, Model model) throws StripeException, IOException {
        PriceListParams priceParams = PriceListParams.builder().addLookupKeys(request.getParameter("lookup_key")).build();
        PriceCollection prices = Price.list(priceParams);
        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(prices.getData().get(0).getId()).setQuantity(1L).build()).setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("http://localhost:8081" + "/success/"+session.getId())
                .setCancelUrl("http://localhost:8081" + "/cancel")
                .build();
        session=Session.create(params);

        model.addAttribute("sessionId",session.getId());
        System.out.println("url sessiombheffg" + session.getUrl());
        response.sendRedirect(session.getUrl());
        return ResponseEntity.ok("subscribed");
    }
    @PostMapping("/create-portal-session")
    public String createSession (HttpServletRequest request,HttpServletResponse response) throws StripeException, IOException{
        // For demonstration purposes, we're using the Checkout session to retrieve the
        // customer ID.
        // Typically, this is stored alongside the authenticated user in your database.
        // Deserialize request from our front end
        Session checkoutSession = Session.retrieve(request.getParameter("session_id"));

        String customer = checkoutSession.getCustomer();
        // Authenticate your user.
        com.stripe.param.billingportal.SessionCreateParams params = new com.stripe.param.billingportal.SessionCreateParams.Builder()
                .setReturnUrl("http://localhost:8081").setCustomer(customer).build();

        com.stripe.model.billingportal.Session portalSession = com.stripe.model.billingportal.Session.create(params);

        response.sendRedirect(portalSession.getUrl());
        return "";
    }

    @PostMapping("/webhook/")
    public String handleStripeEvent(HttpServletRequest request,HttpServletResponse response) throws IOException  {
        String payload = IOUtils.toString(request.getReader());
        Event event = null;
        try {
            event = ApiResource.GSON.fromJson(payload, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            System.out.println("⚠️  Webhook error while parsing basic request.");
            response.setStatus(400);
            return "";
        }
        String sigHeader = request.getHeader("Stripe-Signature");
        System.out.println(sigHeader);
        if (endpointSecret != null && sigHeader != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise, use the basic event deserialized with GSON.
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } catch (SignatureVerificationException e) {
                // Invalid signature
                System.out.println("⚠️  Webhook error while validating signature.");
                response.setStatus(400);
                return "";
            }
        }
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        Subscription subscription = null;
        switch (event.getType()) {
            case "customer.subscription.deleted":
                subscription = (Subscription) stripeObject;
                // Then define and call a function to handle the event
                // customer.subscription.deleted
                // handleSubscriptionTrialEnding(subscription);
            case "customer.subscription.trial_will_end":
                subscription = (Subscription) stripeObject;
                // Then define and call a function to handle the event
                // customer.subscription.trial_will_end
                // handleSubscriptionDeleted(subscriptionDeleted);
            case "customer.subscription.created":
                subscription = (Subscription) stripeObject;
                // Then define and call a function to handle the event
                // customer.subscription.created
                // handleSubscriptionCreated(subscription);
            case "customer.subscription.updated":
                subscription = (Subscription) stripeObject;
                // Then define and call a function to handle the event
                // customer.subscription.updated
                // handleSubscriptionUpdated(subscription);
                // ... handle other event types
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
        response.setStatus(200);
        return "";
    }

}
