package io.dev.med.webhook.connector.infrastructure.adapter.out;

import com.stripe.model.Balance;
import io.dev.med.webhook.connector.domain.model.BalanceWebhook;
import io.dev.med.webhook.connector.domain.model.BalanceWebhook.AvailableWebhook;
import io.dev.med.webhook.connector.domain.model.BalanceWebhook.ConnectReservedWebhook;
import io.dev.med.webhook.connector.domain.model.BalanceWebhook.PendingWebhook;
import io.dev.med.webhook.connector.domain.port.in.WebhookProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BalanceWebhookProcessor implements WebhookProcessor<BalanceWebhook> {

    @Override
    public BalanceWebhook process(Object data) {

        Balance balance = (Balance) data;

        if (balance == null) {
            return null;
        }

        List<AvailableWebhook> availableListConverted = Optional.ofNullable(balance.getAvailable())
                .orElse(Collections.emptyList()).stream()
                .map(stripeAvailable -> new AvailableWebhook(stripeAvailable.getAmount(), stripeAvailable.getCurrency()))
                .toList();

        List<ConnectReservedWebhook> connectReservedListConverted = Optional.ofNullable(balance.getConnectReserved())
                .orElse(Collections.emptyList()).stream()
                .map(stripeConnectReserved -> new ConnectReservedWebhook(stripeConnectReserved.getAmount(), stripeConnectReserved.getCurrency()))
                .toList();

        List<PendingWebhook> pendingListConverted = Optional.ofNullable(balance.getPending())
                .orElse(Collections.emptyList()).stream()
                .map(stripePending -> new PendingWebhook(stripePending.getAmount(), stripePending.getCurrency()))
                .toList();

        return new BalanceWebhook(availableListConverted, connectReservedListConverted,
                balance.getLivemode(), pendingListConverted);
    }
}
