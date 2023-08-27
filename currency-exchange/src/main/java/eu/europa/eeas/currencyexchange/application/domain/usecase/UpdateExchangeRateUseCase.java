package eu.europa.eeas.currencyexchange.application.domain.usecase;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.ports.out.PersistExchangeRatePort;
import eu.europa.eeas.currencyexchange.common.UseCase;
import jakarta.transaction.Transactional;

@UseCase
@Transactional
public class UpdateExchangeRateUseCase implements eu.europa.eeas.currencyexchange.application.ports.in.UpdateExchangeRatePort {

    private final PersistExchangeRatePort persistExchangeRatePort;

    public UpdateExchangeRateUseCase(PersistExchangeRatePort persistExchangeRatePort) {
        this.persistExchangeRatePort = persistExchangeRatePort;
    }

    @Override
    public void updateExchangeRate(CurrencyExchange currencyExchange) {
        persistExchangeRatePort.updateExchangeRate(currencyExchange);
    }
}
