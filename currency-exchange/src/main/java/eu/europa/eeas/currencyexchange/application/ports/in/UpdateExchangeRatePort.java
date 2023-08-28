package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;

public interface UpdateExchangeRatePort {
    OperationResult updateExchangeRate(CurrencyExchange currencyExchange);
}
