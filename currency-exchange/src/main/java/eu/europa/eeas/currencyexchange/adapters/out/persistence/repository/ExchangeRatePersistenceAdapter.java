package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.EventType;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.usecase.NoExchangeRateForPairException;
import eu.europa.eeas.currencyexchange.application.ports.out.PersistExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RetrieveExchangeRatePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Date;
import java.util.Optional;

@Component
public class ExchangeRatePersistenceAdapter implements RetrieveExchangeRatePort, PersistExchangeRatePort {

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
    public void updateExchangeRate(CurrencyExchange currencyExchange) {
        CurrencyExchangeJpaEntity entity = findCurrencyExchangeJpaEntity(
                currencyExchange.getSource(), currencyExchange.getTarget());
        entity.setRate(currencyExchange.getRate());
        repository.save(entity);
        persistEventToOutbox(entity, EventType.UPDATE);
    }

    private CurrencyExchangeJpaEntity findCurrencyExchangeJpaEntity(Currency from, Currency to) {
        return repository.findByCodeFromAndCodeTo(from.getCurrencyCode(), to.getCurrencyCode())
                .orElseThrow(() -> new NoExchangeRateForPairException(from, to));
    }

    @Override
    public Optional<CurrencyExchange> getExchangeRate(Currency from, Currency to) {
        CurrencyExchangeJpaEntity entity = findCurrencyExchangeJpaEntity(from, to);
        CurrencyExchange model = CurrencyExchangeConverter.entityToModel(entity);
        return Optional.of(model);
    }

    private void persistEventToOutbox(CurrencyExchangeJpaEntity entity, EventType eventType) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        OutboxJpaEntity event = new OutboxJpaEntity();
        event.setEventType(eventType);
        event.setEventData(json);
        event.setCreatedAt(new Date());
        outboxRepository.save(event);
    }
}
