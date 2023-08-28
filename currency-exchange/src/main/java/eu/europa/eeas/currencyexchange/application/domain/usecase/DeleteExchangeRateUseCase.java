package eu.europa.eeas.currencyexchange.application.domain.usecase;

import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.in.DeleteExchangeRatePort;
import eu.europa.eeas.currencyexchange.application.ports.out.RemoveExchangeRatePort;
import eu.europa.eeas.currencyexchange.common.UseCase;
import jakarta.transaction.Transactional;

import java.util.Currency;

@UseCase
@Transactional
public class DeleteExchangeRateUseCase implements DeleteExchangeRatePort {

    private final RemoveExchangeRatePort removeExchangeRatePort;

    public DeleteExchangeRateUseCase(RemoveExchangeRatePort removeExchangeRatePort) {
        this.removeExchangeRatePort = removeExchangeRatePort;
    }

    @Override
    public OperationResult delete(Currency source, Currency target) {
        return removeExchangeRatePort.deleteExchangeRate(source, target);
    }
}
