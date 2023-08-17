package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

import java.util.Currency;

public class CurrencyExchangeConverter {

    private CurrencyExchangeConverter() {}

    public static CurrencyExchangeJpaEntity modelToEntity(CurrencyExchange model) {
        CurrencyExchangeJpaEntity entity = new CurrencyExchangeJpaEntity();
        entity.setRate(model.getRate());
        entity.setCodeTo(model.getTarget().getCurrencyCode());
        entity.setCodeFrom(model.getSource().getCurrencyCode());
        return entity;
    }

    public static CurrencyExchange entityToModel(CurrencyExchangeJpaEntity entity) {
        Currency from = Currency.getInstance(entity.getCodeFrom());
        Currency to = Currency.getInstance(entity.getCodeTo());
        return new CurrencyExchange(from, to, entity.getRate());
    }
}
