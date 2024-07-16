package io.dev.med.webhook.connector.domain.model;

import java.util.List;

public record BalanceWebhook(List<AvailableWebhook> available,
                             List<ConnectReservedWebhook> connectReserved,
                             boolean liveMode,
                             List<PendingWebhook> pending) {


    public record AvailableWebhook(Long amount, String currency) {
    }

    public record ConnectReservedWebhook(Long amount, String currency) {
    }

    public record PendingWebhook(Long amount, String currency) {
    }

}
