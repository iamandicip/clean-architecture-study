package eu.europa.eeas.currencyexchange.adapters.in.web;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.domain.model.OperationResult;
import eu.europa.eeas.currencyexchange.application.ports.in.CreateExchangeRatePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;

@RestController
public class CreateExchangeRateController {

    private final CreateExchangeRatePort createExchangeRatePort;

    public CreateExchangeRateController(CreateExchangeRatePort createExchangeRatePort) {
        this.createExchangeRatePort = createExchangeRatePort;
    }

    @PostMapping("/exchange-rate/{from}/{to}/{rate}")
    public ResponseEntity<Void> updateExchangeRate(@PathVariable String from,
                                                   @PathVariable String to,
                                                   @PathVariable Float rate) {
        var fromCurrency = Currency.getInstance(from.toUpperCase());
        var toCurrency = Currency.getInstance(to.toUpperCase());
        var currencyExchange = new CurrencyExchange(fromCurrency, toCurrency, rate);
        var result = createExchangeRatePort.createExchangeRate(currencyExchange);
        var status = result.equals(OperationResult.UPDATE) ? HttpStatus.OK : HttpStatus.CREATED;
        return ResponseEntity.status(status).build();
    }
}
