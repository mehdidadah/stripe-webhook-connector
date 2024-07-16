package io.dev.med.webhook.connector.infrastructure.adapter.out;

import com.stripe.model.Charge;
import io.dev.med.webhook.connector.domain.model.ChargeWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;

public class ChargeWebhookProcessor implements WebhookProcessor<ChargeWebhook> {

    @Override
    public ChargeWebhook process(Object data) {

        Charge charge = (Charge) data;

        if (charge == null) {
            return null;
        }

        return new ChargeWebhook(charge.getId(), charge.getCreated(),
                charge.getCurrency(), charge.getBalanceTransaction());
    }
}
