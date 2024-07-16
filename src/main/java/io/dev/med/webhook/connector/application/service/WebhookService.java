package io.dev.med.webhook.connector.application.service;

import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import io.dev.med.webhook.connector.domain.model.EventStripeWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;
import io.dev.med.webhook.connector.domain.port.out.EventProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    private final String webhookSecret;
    private final EventProcessorFactory eventProcessorFactory;

    public WebhookService(String webhookSecret, EventProcessorFactory eventProcessorFactory) {
        this.webhookSecret = webhookSecret;
        this.eventProcessorFactory = eventProcessorFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> EventStripeWebhook<T> processWebhook(String payload, String sigHeader) {

        // Log the first few characters of the signature for debugging
        String maskedSigHeader = sigHeader.length() > 10 ? sigHeader.substring(0, 10) + "..." : sigHeader;
        log.info("Processing webhook with masked signature header: {}", maskedSigHeader);

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            log.debug("Constructed event: {}", event);

            // Deserialize the nested object inside the event
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject()
                    .orElseThrow(() -> new IllegalArgumentException("Deserialization failed"));
            log.debug("Deserialized Stripe object: {}", stripeObject);

            // Determine the processor based on the class of the Stripe object
            WebhookProcessor<T> processor = eventProcessorFactory.getProcessor((Class<T>) stripeObject.getClass());
            if (processor != null) {
                log.debug("Found processor for class: {}", stripeObject.getClass().getSimpleName());

                // Process the Stripe object and convert it to your domain model
                T data = processor.process(stripeObject);

                log.info("Processed data for event id: {}", event.getId());

                // Create and return the EventStripeWebhook
                return new EventStripeWebhook<>(
                        event.getId(),
                        event.getAccount(),
                        event.getType(),
                        event.getCreated(),
                        data);

            } else {
                log.error("No processor found for event type: {}", event.getType());
                throw new IllegalArgumentException("No processor found for event type: " + event.getType());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Webhook processing failed", e);
        }
    }
}
