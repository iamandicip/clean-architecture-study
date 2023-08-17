package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

public interface UpdateExchangeRatePort {
    void updateExchangeRate(CurrencyExchange currencyExchange);
}
