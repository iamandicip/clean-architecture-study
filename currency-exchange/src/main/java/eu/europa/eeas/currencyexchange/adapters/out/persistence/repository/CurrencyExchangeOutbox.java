package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CurrencyExchangeOutbox {
    private String source;
    private String target;
    private Float rate;
    private String idempotencyKey;
    private String operationType;

    public static CurrencyExchangeOutbox create(CurrencyExchangeJpaEntity jpaEntity, String operationType) {
        return CurrencyExchangeOutbox.builder()
                .source(jpaEntity.getCodeFrom())
                .target(jpaEntity.getCodeTo())
                .rate(jpaEntity.getRate())
                .operationType(operationType)
                // we need this id on the consumer side, in order to ensure operation idempotency
                .idempotencyKey(calculateIdempotencyKey(jpaEntity))
                .build();
    }

    private static String calculateIdempotencyKey(CurrencyExchangeJpaEntity entity) {
        // it's a good idea to give a meaning to the generated ids,
        // so it's easier to understand to which kind of entity they belong
        return "EXCH-" + entity.getCodeFrom() + entity.getCodeTo() + "-" + UUID.randomUUID();
    }
}
