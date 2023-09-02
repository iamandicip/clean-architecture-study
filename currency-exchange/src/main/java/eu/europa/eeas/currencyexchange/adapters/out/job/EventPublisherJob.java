package eu.europa.eeas.currencyexchange.adapters.out.job;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.repository.OutboxPersistenceAdapter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class EventPublisherJob {

    private final OutboxPersistenceAdapter outboxPersistenceAdapter;
    private final EventPublisher eventPublisher;

    public EventPublisherJob(OutboxPersistenceAdapter outboxPersistenceAdapter, EventPublisher eventPublisher) {
        this.outboxPersistenceAdapter = outboxPersistenceAdapter;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    @Transactional
    public void pollOutbox() {
        log.debug("Starting publishing scheduled job");
        try {
            List<OutboxJpaEntity> events = outboxPersistenceAdapter.listAllEvents();
            for (var event : events) {
                publishEvent(event);
                outboxPersistenceAdapter.deleteEvent(event);
            }
        } catch (Exception e) {
            log.error("Unexpected error publishing events", e);
        }
    }

    private void publishEvent(OutboxJpaEntity event) throws IOException, TimeoutException {
        log.debug("Publishing event {}", event.getPayload());
        eventPublisher.publishMessage(event.getPayload(), event.getEventType().name());
    }
}
