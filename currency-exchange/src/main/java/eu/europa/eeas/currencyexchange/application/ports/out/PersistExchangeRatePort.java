package eu.europa.eeas.currencyexchange.application.ports.out;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.common.PersistenceAdapter;

@PersistenceAdapter
public interface PersistExchangeRatePort {
    void updateExchangeRate(CurrencyExchange currencyExchange);
}
