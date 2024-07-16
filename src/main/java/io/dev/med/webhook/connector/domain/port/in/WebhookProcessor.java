package io.dev.med.webhook.connector.domain.port.in;

public interface WebhookProcessor<T> {

    T process(Object data);
}
