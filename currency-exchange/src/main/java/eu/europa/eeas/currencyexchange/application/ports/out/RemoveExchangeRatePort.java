package eu.europa.eeas.currencyexchange.application.ports.out;

import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;

import java.util.Currency;

public interface RemoveExchangeRatePort {
    OperationResult deleteExchangeRate(Currency source, Currency target);
}
