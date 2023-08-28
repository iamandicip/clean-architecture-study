package eu.europa.eeas.currencyexchange.application.ports.out;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.common.PersistenceAdapter;

@PersistenceAdapter
public interface PersistExchangeRatePort {
    OperationResult updateExchangeRate(CurrencyExchange currencyExchange);
}
