package eu.europa.eeas.currencyexchange.adapters.in.web;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.in.UpdateExchangeRatePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;

@RestController
public class UpdateExchangeRateController {

    private final UpdateExchangeRatePort updateExchangeRatePort;

    @Autowired
    public UpdateExchangeRateController(UpdateExchangeRatePort updateExchangeRatePort) {
        this.updateExchangeRatePort = updateExchangeRatePort;
    }

    @PutMapping("/exchange-rate/{from}/{to}/{rate}")
    public ResponseEntity<Void> updateExchangeRate(@PathVariable String from,
                                                   @PathVariable String to,
                                                   @PathVariable Float rate) {
        var fromCurrency = Currency.getInstance(from.toUpperCase());
        var toCurrency = Currency.getInstance(to.toUpperCase());
        var currencyExchange = new CurrencyExchange(fromCurrency, toCurrency, rate);
        var result = updateExchangeRatePort.updateExchangeRate(currencyExchange);
        var status = result.equals(OperationResult.CREATE) ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).build();
    }
}
