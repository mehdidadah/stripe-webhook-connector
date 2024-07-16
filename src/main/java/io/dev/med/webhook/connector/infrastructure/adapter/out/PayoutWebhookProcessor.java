package io.dev.med.webhook.connector.infrastructure.adapter.out;

import com.stripe.model.Payout;
import io.dev.med.webhook.connector.domain.model.PayoutWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;

public class PayoutWebhookProcessor implements WebhookProcessor<PayoutWebhook> {

    @Override
    public PayoutWebhook process(Object data) {
        Payout payout = (Payout) data;

        if (payout == null) {
            return null;
        }

        return new PayoutWebhook(payout.getId(), payout.getStatus(),
                payout.getType(), payout.getAmount());
    }
}
