package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.EventType;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.out.NewExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.PersistExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RemoveExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RetrieveExchangeRatePort;
import eu.europa.eeas.currencyexchange.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class ExchangeRatePersistenceAdapter implements
        RetrieveExchangeRatePort,
        PersistExchangeRatePort,
        NewExchangeRatePort,
        RemoveExchangeRatePort {

    private final CurrencyExchangeJpaRepository repository;
    private final OutboxJpaRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExchangeRatePersistenceAdapter(CurrencyExchangeJpaRepository repository,
                                          OutboxJpaRepository outboxRepository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public OperationResult createExchangeRate(CurrencyExchange currencyExchange) {
        // design decision: make the create operation idempotent, too
        return createOrUpdateExchangeRate(currencyExchange);
    }

    @Override
    public Optional<CurrencyExchange> getExchangeRate(Currency from, Currency to) {
        return findCurrencyExchangeJpaEntity(from, to).map(CurrencyExchangeConverter::entityToModel);
    }

    @Override
    public OperationResult updateExchangeRate(CurrencyExchange currencyExchange) {
        // update should be an idempotent operation, that creates the resource if it doesn't already exist
        return createOrUpdateExchangeRate(currencyExchange);
    }

    private OperationResult createOrUpdateExchangeRate(CurrencyExchange currencyExchange) {
        CurrencyExchangeJpaEntity entity;
        OperationResult result;
        EventType eventType;

        Optional<CurrencyExchangeJpaEntity> entityOpt = findCurrencyExchangeJpaEntity(
                currencyExchange.getSource(), currencyExchange.getTarget());

        if (entityOpt.isEmpty()) {
            entity = CurrencyExchangeConverter.modelToEntity(currencyExchange);
            result = OperationResult.CREATE;
            eventType = EventType.CREATE;
        } else {
            entity = entityOpt.get();
            result = OperationResult.UPDATE;
            eventType = EventType.UPDATE;
        }

        repository.save(entity);
        persistEventToOutbox(entity, eventType);

        return result;
    }

    @Override
    public OperationResult deleteExchangeRate(Currency source, Currency target) {
        Optional<CurrencyExchangeJpaEntity> entityOpt = findCurrencyExchangeJpaEntity(source, target);

        if (entityOpt.isEmpty()) {
            return OperationResult.NOOP;
        }

        CurrencyExchangeJpaEntity currencyExchangeEntity = entityOpt.get();
        repository.delete(currencyExchangeEntity);
        persistEventToOutbox(currencyExchangeEntity, EventType.DELETE);

        return OperationResult.DELETE;
    }

    private Optional<CurrencyExchangeJpaEntity> findCurrencyExchangeJpaEntity(Currency from, Currency to) {
        return repository.findByCodeFromAndCodeTo(from.getCurrencyCode(), to.getCurrencyCode());
    }

    private void persistEventToOutbox(CurrencyExchangeJpaEntity entity, EventType eventType) {
        try {
            CurrencyExchangeOutbox model = CurrencyExchangeOutbox.builder()
                    .source(entity.getCodeFrom())
                    .target(entity.getCodeTo())
                    .rate(entity.getRate())
                    .operationType(eventType.name())
                    // we need this id on the consumer side, in order to ensure operation idempotency
                    .idempotencyId(UUID.randomUUID().toString())
                    .build();
            String json = objectMapper.writeValueAsString(model);
            OutboxJpaEntity event = new OutboxJpaEntity();
            event.setEventType(eventType);
            event.setAggregateId(entity.getId());
            event.setAggregateType("CurrencyExchange");
            event.setPayload(json);
            event.setCreatedAt(new Date());
            outboxRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new ApplicationException("There was an unexpected error processing the changes", e);
        }
    }
}
