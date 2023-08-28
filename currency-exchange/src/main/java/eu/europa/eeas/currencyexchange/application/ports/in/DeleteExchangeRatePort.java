package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;

import java.util.Currency;

public interface DeleteExchangeRatePort {
    public OperationResult delete(Currency source, Currency target);
}
