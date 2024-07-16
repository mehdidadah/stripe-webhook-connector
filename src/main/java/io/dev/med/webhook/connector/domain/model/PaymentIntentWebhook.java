package io.dev.med.webhook.connector.domain.model;

public record PaymentIntentWebhook(
        String id, //The unique identifier of the payment intention
        Long amount, //The amount to pay
        String currency, //The currency of payment
        String status, //The current state of payment intention
        Long created, //The date the payment intention was created
        String description, // A description of the payment intention
        boolean liveMode //Indicates whether the payment intent was created in live mode or in test mode
) {
}
