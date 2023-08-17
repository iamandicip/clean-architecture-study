package eu.europa.eeas.currencyexchange.adapters.in.web.dto;

import eu.europa.eeas.currencyexchange.adapters.in.web.dto.CurrencyExchangeDto;
import eu.europa.eeas.currencyexchange.application.domain.model.CurrencyExchange;

public class CurrencyExchangeDtoConverter {

    private CurrencyExchangeDtoConverter() {
    }

    public static CurrencyExchangeDto modelToDto(CurrencyExchange model) {
        return new CurrencyExchangeDto(model.getSource().getCurrencyCode(),
                model.getTarget().getCurrencyCode(),
                model.getRate());
    }
}
