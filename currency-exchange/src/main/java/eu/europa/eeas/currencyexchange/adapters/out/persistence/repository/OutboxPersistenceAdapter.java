package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.EventType;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.common.ApplicationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OutboxPersistenceAdapter {
    private final OutboxJpaRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxPersistenceAdapter(OutboxJpaRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public List<OutboxJpaEntity> listAllEvents() {
        return repository.findAllOrderByCreatedAt();
    }

    public void deleteEvent(OutboxJpaEntity event) {
        repository.delete(event);
    }

    public void createEvent(CurrencyExchangeJpaEntity entity, EventType eventType) {
        try {
            var model = CurrencyExchangeOutbox.create(entity, eventType.name());
            var json = objectMapper.writeValueAsString(model);
            var event = new OutboxJpaEntity();
            event.setEventType(eventType);
            event.setAggregateId(entity.getId());
            event.setAggregateType("CurrencyExchange");
            event.setPayload(json);
            event.setCreatedAt(new Date());
            repository.save(event);
        } catch (JsonProcessingException e) {
            throw new ApplicationException("There was an unexpected error processing the changes", e);
        }
    }
}
