package pl.sswrk.currencycalculator.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sswrk.currencycalculator.model.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Component
@AllArgsConstructor
public class MoneyConverter{

    private final ExchangeRateResource exchangeRateResource;

    public Money convert(Money source, Currency desiredCurrency){
        BigDecimal amount = source.getAmount()
                .multiply(exchangeRateResource.getExchangeRate(source.getCurrency(), desiredCurrency))
                .setScale(2, RoundingMode.HALF_EVEN);
        return new Money(desiredCurrency, amount);
    }

}
