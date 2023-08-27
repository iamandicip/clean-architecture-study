package eu.europa.eeas.currencyexchange.application.domain.usecase;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

public class ExchangeRatePairAlreadyExistsException extends RuntimeException {
    public ExchangeRatePairAlreadyExistsException(CurrencyExchange currencyExchange) {
        super(String.format("Exchange rate from %s to %s already exists!",
                currencyExchange.getSource().getCurrencyCode(), currencyExchange.getTarget().getCurrencyCode()));
    }
}
