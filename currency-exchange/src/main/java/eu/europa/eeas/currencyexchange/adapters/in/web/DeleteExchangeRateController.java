package eu.europa.eeas.currencyexchange.adapters.in.web;

import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.in.DeleteExchangeRatePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;

@RestController
public class DeleteExchangeRateController {
    private final DeleteExchangeRatePort deleteExchangeRatePort;

    public DeleteExchangeRateController(DeleteExchangeRatePort deleteExchangeRatePort) {
        this.deleteExchangeRatePort = deleteExchangeRatePort;
    }

    @DeleteMapping("/exchange-rate/{from}/{to}")
    public ResponseEntity<Void> updateExchangeRate(@PathVariable String from,
                                                   @PathVariable String to) {
        var fromCurrency = Currency.getInstance(from.toUpperCase());
        var toCurrency = Currency.getInstance(to.toUpperCase());
        var result = deleteExchangeRatePort.delete(fromCurrency, toCurrency);
        var status = result.equals(OperationResult.DELETE) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).build();
    }
}
