package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;

public interface CreateExchangeRatePort {
    OperationResult createExchangeRate(CurrencyExchange currencyExchange);
}
