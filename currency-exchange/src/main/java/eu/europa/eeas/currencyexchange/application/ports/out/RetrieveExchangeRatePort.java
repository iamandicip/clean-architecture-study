package eu.europa.eeas.currencyexchange.application.ports.out;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

import java.util.Currency;
import java.util.Optional;

public interface RetrieveExchangeRatePort {
    Optional<CurrencyExchange> getExchangeRate(Currency source, Currency target);
}
