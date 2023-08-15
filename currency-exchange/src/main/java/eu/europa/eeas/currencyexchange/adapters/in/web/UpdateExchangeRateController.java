package eu.europa.eeas.currencyexchange.adapters.in.web;

import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;
import eu.europa.eeas.currencyexchange.application.ports.in.UpdateExchangeRatePort;
import org.springframework.beans.factory.annotation.Autowired;
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
        Currency fromCurrency = Currency.getInstance(from.toUpperCase());
        Currency toCurrency = Currency.getInstance(to.toUpperCase());
        CurrencyExchange currencyExchange = new CurrencyExchange(fromCurrency, toCurrency, rate);
        updateExchangeRatePort.updateExchangeRate(currencyExchange);
        return ResponseEntity.ok().build();
    }

}
