package pl.sswrk.currencycalculator.services;

import java.math.BigDecimal;
import java.util.Currency;

public interface ExchangeRateResource {

    public BigDecimal getExchangeRate(Currency from, Currency to);

}
