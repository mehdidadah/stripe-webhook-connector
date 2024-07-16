package io.dev.med.webhook.connector.domain.model;

public record ChargeWebhook(String id,
                            Long amount,
                            String currency,
                            String balanceTransaction) {
}
