package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyExchangeOutbox {
    private String source;
    private String target;
    private Float rate;
    private String idempotencyId;
    private String operationType;
}
