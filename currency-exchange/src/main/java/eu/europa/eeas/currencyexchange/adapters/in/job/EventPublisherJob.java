package eu.europa.eeas.currencyexchange.adapters.in.job;

import eu.europa.eeas.currencyexchange.adapters.in.job.rabbitmq.RabbitMqPublisher;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.repository.OutboxJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class EventPublisherJob {

    private final OutboxJpaRepository repository;
    private final EventPublisher eventPublisher;

    public EventPublisherJob(OutboxJpaRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void pollOutbox() {
        log.debug("Starting publishing scheduled job");
        try {
            List<OutboxJpaEntity> events = repository.findAllOrderByCreatedAt();
            for (OutboxJpaEntity event : events) {
                publishEvent(event);
                repository.delete(event);
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
