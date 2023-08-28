package eu.europa.eeas.currencyexchange.application.domain.usecase;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.in.CreateExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.NewExchangeRatePort;
import eu.europa.eeas.currencyexchange.common.UseCase;
import jakarta.transaction.Transactional;

@UseCase
@Transactional
public class CreateExchangeRateUseCase implements CreateExchangeRatePort {

    private final NewExchangeRatePort newExchangeRatePort;

    public CreateExchangeRateUseCase(NewExchangeRatePort newExchangeRatePort) {
        this.newExchangeRatePort = newExchangeRatePort;
    }

    @Override
    public OperationResult createExchangeRate(CurrencyExchange currencyExchange) {
        return this.newExchangeRatePort.createExchangeRate(currencyExchange);
    }
}
