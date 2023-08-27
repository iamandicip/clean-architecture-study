package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

import java.util.Currency;

public interface DeleteExchangeRatePort {
    public void delete(Currency source, Currency target);
}
