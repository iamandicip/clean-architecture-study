package eu.europa.eeas.currencyexchange.application.domain.usecase;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
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
    public void createExchangeRate(CurrencyExchange currencyExchange) {
        // business validations are made in the application domain
        validateExchangeRateDoesNotExist(currencyExchange);
        this.newExchangeRatePort.createExchangeRate(currencyExchange);
    }

    private void validateExchangeRateDoesNotExist(CurrencyExchange currencyExchange) {
        if (!newExchangeRatePort.checkExchangeRateDoesNotExist(currencyExchange)) {
            throw new ExchangeRatePairAlreadyExistsException(currencyExchange);
        }
    }
}
