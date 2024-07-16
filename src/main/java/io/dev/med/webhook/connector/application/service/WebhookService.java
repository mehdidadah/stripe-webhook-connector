package io.dev.med.webhook.connector.application.service;

import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import io.dev.med.webhook.connector.domain.model.EventStripeWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;
import io.dev.med.webhook.connector.domain.port.out.EventProcessorFactory;

public class WebhookService {

    private final String webhookSecret;
    private final EventProcessorFactory eventProcessorFactory;

    public WebhookService(String webhookSecret, EventProcessorFactory eventProcessorFactory) {
        this.webhookSecret = webhookSecret;
        this.eventProcessorFactory = eventProcessorFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> EventStripeWebhook<T> processWebhook(String payload, String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // Deserialize the nested object inside the event
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject()
                    .orElseThrow(() -> new IllegalArgumentException("Deserialization failed"));


            // Determine the processor based on the class of the Stripe object
            WebhookProcessor<T> processor = eventProcessorFactory.getProcessor((Class<T>) stripeObject.getClass());
            if (processor != null) {

                // Process the Stripe object and convert it to your domain model
                T data = processor.process(stripeObject);

                // Create and return the EventStripeWebhook
                return new EventStripeWebhook<>(
                        event.getId(),
                        event.getAccount(),
                        event.getType(),
                        event.getCreated(),
                        data);

            } else {
                throw new IllegalArgumentException("No processor found for event type: " + event.getType());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Webhook processing failed", e);
        }
    }
}
