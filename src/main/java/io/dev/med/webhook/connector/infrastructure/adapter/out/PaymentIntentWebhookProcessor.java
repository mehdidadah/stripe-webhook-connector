package io.dev.med.webhook.connector.infrastructure.adapter.out;

import com.stripe.model.PaymentIntent;
import io.dev.med.webhook.connector.domain.model.PaymentIntentWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;

public class PaymentIntentWebhookProcessor implements WebhookProcessor<PaymentIntentWebhook> {

    @Override
    public PaymentIntentWebhook process(Object data) {
        PaymentIntent paymentIntent = (PaymentIntent) data;

        if (paymentIntent == null) {
            return null;
        }

        return new PaymentIntentWebhook(paymentIntent.getId(), paymentIntent.getAmount(),
                paymentIntent.getCurrency());
    }
}
