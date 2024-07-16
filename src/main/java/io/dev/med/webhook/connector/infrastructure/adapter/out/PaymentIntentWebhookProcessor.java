package io.dev.med.webhook.connector.infrastructure.adapter.out;

import com.stripe.model.PaymentIntent;
import io.dev.med.webhook.connector.domain.model.PaymentIntentWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentIntentWebhookProcessor implements WebhookProcessor<PaymentIntentWebhook> {

    private static final Logger log = LoggerFactory.getLogger(PaymentIntentWebhookProcessor.class);

    @Override
    public PaymentIntentWebhook process(Object data) {
        PaymentIntent paymentIntent = (PaymentIntent) data;

        if (paymentIntent == null) {
            return null;
        }

        log.debug("Processing PaymentIntent with id: {}", paymentIntent.getId());

        return new PaymentIntentWebhook(paymentIntent.getId(), paymentIntent.getAmount(),
                paymentIntent.getCurrency(), paymentIntent.getStatus(), paymentIntent.getCreated(),
                paymentIntent.getDescription(), paymentIntent.getLivemode());
    }
}
