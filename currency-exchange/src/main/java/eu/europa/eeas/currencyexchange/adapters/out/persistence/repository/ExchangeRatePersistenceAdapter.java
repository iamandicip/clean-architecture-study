package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.usecase.NoExchangeRateForPairException;
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
    public void createExchangeRate(CurrencyExchange currencyExchange) {
        CurrencyExchangeJpaEntity entity = new CurrencyExchangeJpaEntity();
        entity.setCodeFrom(currencyExchange.getSource().getCurrencyCode());
        entity.setCodeTo(currencyExchange.getTarget().getCurrencyCode());
        entity.setRate(currencyExchange.getRate());
        repository.save(entity);
        persistEventToOutbox(entity, OutboxJpaEntity.EventType.CREATE);
    }

    @Override
    public Optional<CurrencyExchange> getExchangeRate(Currency from, Currency to) {
        CurrencyExchangeJpaEntity entity = findCurrencyExchangeJpaEntity(from, to);
        CurrencyExchange model = CurrencyExchangeConverter.entityToModel(entity);
        return Optional.of(model);
    }

    @Override
    public void updateExchangeRate(CurrencyExchange currencyExchange) {
        CurrencyExchangeJpaEntity entity = findCurrencyExchangeJpaEntity(
                currencyExchange.getSource(), currencyExchange.getTarget());
        entity.setRate(currencyExchange.getRate());
        entity = repository.save(entity);
        persistEventToOutbox(entity, OutboxJpaEntity.EventType.UPDATE);
    }

    @Override
    public void deleteExchangeRate(Currency source, Currency target) {
        CurrencyExchangeJpaEntity entity = findCurrencyExchangeJpaEntity(source, target);
        repository.delete(entity);
        persistEventToOutbox(entity, OutboxJpaEntity.EventType.DELETE);
    }

    @Override
    public boolean checkExchangeRateDoesNotExist(CurrencyExchange currencyExchange) {
        return repository.findByCodeFromAndCodeTo(
                        currencyExchange.getSource().getCurrencyCode(), currencyExchange.getTarget().getCurrencyCode())
                .isEmpty();
    }

    private CurrencyExchangeJpaEntity findCurrencyExchangeJpaEntity(Currency from, Currency to) {
        // throwing the exception here is not a business validation, it is an input validation
        // that is why it doesn't belong in the domain
        return repository.findByCodeFromAndCodeTo(from.getCurrencyCode(), to.getCurrencyCode())
                .orElseThrow(() -> new NoExchangeRateForPairException(from, to));
    }

    private void persistEventToOutbox(CurrencyExchangeJpaEntity entity, OutboxJpaEntity.EventType eventType) {
        try {
            CurrencyExchange model = CurrencyExchangeConverter.entityToModel(entity);
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
