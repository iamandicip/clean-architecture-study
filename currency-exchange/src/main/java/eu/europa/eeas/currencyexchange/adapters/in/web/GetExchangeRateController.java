package eu.europa.eeas.currencyexchange.adapters.in.web;

import eu.europa.eeas.currencyexchange.adapters.in.web.dto.CurrencyExchangeDto;
import eu.europa.eeas.currencyexchange.adapters.in.web.dto.CurrencyExchangeDtoConverter;
import eu.europa.eeas.currencyexchange.application.ports.in.GetExchangeRatePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.Optional;

@RestController
public class GetExchangeRateController {
    private final GetExchangeRatePort getExchangeRatePort;

    @Autowired
    public GetExchangeRateController(GetExchangeRatePort getExchangeRatePort) {
        this.getExchangeRatePort = getExchangeRatePort;
    }

    @GetMapping("/exchange-rate/{from}/{to}")
    public ResponseEntity<CurrencyExchangeDto> getExchangeRate(@PathVariable String from, @PathVariable String to) {
        Currency currencyFrom = Currency.getInstance(from.toUpperCase());
        Currency currencyTo = Currency.getInstance(to.toUpperCase());
        Optional<CurrencyExchangeDto> dto = getExchangeRatePort.getExchangeRate(currencyFrom, currencyTo)
                .map(CurrencyExchangeDtoConverter::modelToDto);
        return ResponseEntity.of(dto);
    }
}
