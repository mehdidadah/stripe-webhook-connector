package io.dev.med.webhook.connector.domain.model;

public record PayoutWebhook(String id, String status, String type, Long amount) {
}
