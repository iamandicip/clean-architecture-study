package eu.europa.eeas.currencyexchange.application.domain.model;

import java.util.Currency;

public class CurrencyExchange {
    private final Currency source;
    private final Currency target;
    private Float rate;

    public CurrencyExchange(Currency source, Currency target, Float rate) {
        this.source = source;
        this.target = target;
        this.rate = rate;
    }

    public Currency getSource() {
        return source;
    }

    public Currency getTarget() {
        return target;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
