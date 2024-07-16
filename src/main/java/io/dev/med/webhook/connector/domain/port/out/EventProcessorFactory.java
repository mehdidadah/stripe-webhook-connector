package io.dev.med.webhook.connector.domain.port.out;

import com.stripe.model.PaymentIntent;
import com.stripe.model.Payout;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;
import io.dev.med.webhook.connector.infrastructure.adapter.out.PaymentIntentWebhookProcessor;
import io.dev.med.webhook.connector.infrastructure.adapter.out.PayoutWebhookProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EventProcessorFactory {

    private final Map<Class<?>, Supplier<WebhookProcessor<?>>> processors = new HashMap<>();

    public EventProcessorFactory() {
        processors.put(PaymentIntent.class, PaymentIntentWebhookProcessor::new);
        processors.put(Payout.class, PayoutWebhookProcessor::new);
    }

    @SuppressWarnings("unchecked")
    public <T> WebhookProcessor<T> getProcessor(Class<T> clazz) {
        Supplier<WebhookProcessor<?>> processorSupplier = processors.get(clazz);
        if (processorSupplier == null) {
            throw new IllegalArgumentException("No processor found for type: " + clazz);
        }
        return (WebhookProcessor<T>) processorSupplier.get();
    }
}
