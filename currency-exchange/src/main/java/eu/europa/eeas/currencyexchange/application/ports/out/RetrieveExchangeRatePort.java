package eu.europa.eeas.currencyexchange.application.ports.out;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.common.PersistenceAdapter;
import eu.europa.eeas.currencyexchange.common.UseCase;

import java.util.Currency;
import java.util.Optional;

@PersistenceAdapter
public interface RetrieveExchangeRatePort {
    Optional<CurrencyExchange> getExchangeRate(Currency source, Currency target);
}
