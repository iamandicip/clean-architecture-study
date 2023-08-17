package eu.europa.eeas.currencyexchange.application.domain.usecase;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.ports.in.GetExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RetrieveExchangeRatePort;
import eu.europa.eeas.currencyexchange.common.UseCase;

import java.util.Currency;
import java.util.Optional;

@UseCase
public class GetExchangeRateUseCase implements GetExchangeRatePort {

    private final RetrieveExchangeRatePort retrieveExchangeRatePort;

    public GetExchangeRateUseCase(RetrieveExchangeRatePort retrieveExchangeRatePort) {
        this.retrieveExchangeRatePort = retrieveExchangeRatePort;
    }

    @Override
    public Optional<CurrencyExchange> getExchangeRate(Currency source, Currency target) {
        return retrieveExchangeRatePort.getExchangeRate(source, target);
    }
}
