package eu.europa.eeas.currencyexchange.adapters.in.job;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface EventPublisher {
    void publishMessage(String payload, String eventType) throws IOException, TimeoutException;
}
