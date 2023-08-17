package eu.europa.eeas.currencyexchange.adapters.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyExchangeDto {
    private String from;
    private String to;
    private Float rate;
}
