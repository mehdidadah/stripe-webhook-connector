package io.dev.med.webhook.connector.domain.model;


public record EventStripeWebhook<R>(String id,
                                 String accountId,
                                 String type,
                                 Long created,
                                 R data) {
}
