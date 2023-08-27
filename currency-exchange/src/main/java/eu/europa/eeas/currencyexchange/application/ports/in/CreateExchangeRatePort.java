package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

public interface CreateExchangeRatePort {
    void createExchangeRate(CurrencyExchange currencyExchange);
}
