package pl.sswrk.currencycalculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sswrk.currencycalculator.services.NbpExchangeRateResource;

import java.math.BigDecimal;
import java.util.Currency;

@SpringBootTest
public class NbpExchangeRateResourceTests {

    @Autowired
    private NbpExchangeRateResource nbpExchangeRateResource;

    @Test
    public void shouldReturnDecimalValue(){
        //given
        Currency currencyFrom = Currency.getInstance("GBP");
        Currency currencyTo = Currency.getInstance("PLN");

        //when
        BigDecimal exchangeRate = nbpExchangeRateResource.getExchangeRate(currencyFrom, currencyTo);

        //then
        Assertions.assertThat(exchangeRate!=null);
        Assertions.assertThat(exchangeRate.compareTo(new BigDecimal("0.00")) > 0);
    }

    @Test
    public void shouldReturnOneIfCurrenciesAreSame(){
        Currency currencyFrom = Currency.getInstance("GBP");
        Currency currencyTo = Currency.getInstance("GBP");

        //when
        BigDecimal exchangeRate = nbpExchangeRateResource.getExchangeRate(currencyFrom, currencyTo);

        //then
        Assertions.assertThat(exchangeRate.equals(new BigDecimal("1.00")));
    }

}
