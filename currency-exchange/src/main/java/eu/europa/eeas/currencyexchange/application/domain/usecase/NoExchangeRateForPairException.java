package eu.europa.eeas.currencyexchange.application.domain.usecase;

import java.util.Currency;

public class NoExchangeRateForPairException extends RuntimeException{
    public NoExchangeRateForPairException(Currency source, Currency target) {
        super(String.format("No exchange rate was found from %s to %s!", source.getCurrencyCode(), target.getCurrencyCode()));
    }
}
