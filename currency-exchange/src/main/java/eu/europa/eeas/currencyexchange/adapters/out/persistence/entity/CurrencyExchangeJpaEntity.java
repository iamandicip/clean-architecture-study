package eu.europa.eeas.currencyexchange.adapters.out.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_rates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchangeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_rates_seq")
    @SequenceGenerator(name = "exchange_rates_seq", sequenceName = "exchange_rates_seq", allocationSize = 1)
    @JsonIgnore
    private Long id;

    private String codeFrom;

    private String codeTo;

    private Float rate;
}
