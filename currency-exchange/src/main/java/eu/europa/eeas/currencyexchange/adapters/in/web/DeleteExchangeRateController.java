package eu.europa.eeas.currencyexchange.adapters.in.web;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.ports.in.DeleteExchangeRatePort;
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
        Currency fromCurrency = Currency.getInstance(from.toUpperCase());
        Currency toCurrency = Currency.getInstance(to.toUpperCase());
        deleteExchangeRatePort.delete(fromCurrency, toCurrency);
        return ResponseEntity.ok().build();
    }
}
