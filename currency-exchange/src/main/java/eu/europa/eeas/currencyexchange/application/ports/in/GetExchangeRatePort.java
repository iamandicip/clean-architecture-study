package eu.europa.eeas.currencyexchange.application.ports.in;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

import java.util.Currency;
import java.util.Optional;

public interface GetExchangeRatePort {
    Optional<CurrencyExchange> getExchangeRate(Currency source, Currency target);
}
