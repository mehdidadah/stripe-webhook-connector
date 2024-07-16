package io.dev.med.webhook.connector.domain.model;

public record PaymentIntentWebhook(String id,
                                   Long amount,
                                   String currency) {
}
