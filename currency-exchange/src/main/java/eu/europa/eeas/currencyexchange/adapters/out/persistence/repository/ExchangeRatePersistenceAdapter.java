package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.EventType;
import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.out.NewExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.PersistExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RemoveExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RetrieveExchangeRatePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Optional;

@Component
public class ExchangeRatePersistenceAdapter implements
        RetrieveExchangeRatePort,
        PersistExchangeRatePort,
        NewExchangeRatePort,
        RemoveExchangeRatePort {

    private final CurrencyExchangeJpaRepository repository;

    private final OutboxPersistenceAdapter outboxPersistenceAdapter;

    @Autowired
    public ExchangeRatePersistenceAdapter(CurrencyExchangeJpaRepository repository,
                                          OutboxPersistenceAdapter outboxPersistenceAdapter) {
        this.repository = repository;
        this.outboxPersistenceAdapter = outboxPersistenceAdapter;
    }

    @Override
    public OperationResult createExchangeRate(CurrencyExchange currencyExchange) {
        // this decision is more a requirement than a technical decision
        // the question is: what should the service do if object already exist?
        // in this case, we decide to allow repeated calls and make the create operation idempotent
        return createOrUpdateExchangeRate(currencyExchange);
    }

    @Override
    public Optional<CurrencyExchange> getExchangeRate(Currency from, Currency to) {
        return findCurrencyExchangeJpaEntity(from, to).map(CurrencyExchangeConverter::entityToModel);
    }

    @Override
    public OperationResult updateExchangeRate(CurrencyExchange currencyExchange) {
        // update should be an idempotent operation; it creates the resource if it doesn't already exist
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
            entity.setRate(currencyExchange.getRate());
            result = OperationResult.UPDATE;
            eventType = EventType.UPDATE;
        }

        repository.save(entity);
        outboxPersistenceAdapter.createEvent(entity, eventType);

        return result;
    }

    @Override
    public OperationResult deleteExchangeRate(Currency source, Currency target) {
        Optional<CurrencyExchangeJpaEntity> entityOpt = findCurrencyExchangeJpaEntity(source, target);

        if (entityOpt.isEmpty()) {
            return OperationResult.NOOP;
        }

        var currencyExchangeEntity = entityOpt.get();
        repository.delete(currencyExchangeEntity);
        outboxPersistenceAdapter.createEvent(currencyExchangeEntity, EventType.DELETE);

        return OperationResult.DELETE;
    }

    private Optional<CurrencyExchangeJpaEntity> findCurrencyExchangeJpaEntity(Currency from, Currency to) {
        return repository.findByCodeFromAndCodeTo(from.getCurrencyCode(), to.getCurrencyCode());
    }
}
