package eu.europa.eeas.currencyexchange.application.ports.out;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

import java.util.Currency;

public interface RemoveExchangeRatePort {
    void deleteExchangeRate(Currency source, Currency target);
}
