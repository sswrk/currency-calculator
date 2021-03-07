package pl.sswrk.currencycalculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sswrk.currencycalculator.model.Money;
import pl.sswrk.currencycalculator.services.ExchangeRateResource;
import pl.sswrk.currencycalculator.services.MoneyConverter;

import java.math.BigDecimal;
import java.util.Currency;

@SpringBootTest
public class MoneyConverterTests {

    @InjectMocks
    private MoneyConverter moneyConverter;

    @Mock
    private ExchangeRateResource exchangeRateResource;

    @Test
    public void shouldCalculateCorrectCurrencyAndAmount(){

        //given
        Currency fromCurrency = Currency.getInstance("GBP");
        Money toConvertFrom = new Money(fromCurrency, new BigDecimal("2.19"));
        Currency toCurrency = Currency.getInstance("PLN");
        BigDecimal fromCurrencyToCurrencyRate = new BigDecimal("5.33");

        Mockito.when(exchangeRateResource.getExchangeRate(fromCurrency, toCurrency))
            .thenReturn(fromCurrencyToCurrencyRate);

        //when
        Money calculated = moneyConverter.convert(toConvertFrom, toCurrency);

        //then
        Assertions.assertThat(calculated).isEqualTo(new Money(toCurrency, new BigDecimal("11.67")));
    }

    @Test
    public void shouldReturnZeroIfZeroOnInput(){

        //given
        Currency fromCurrency = Currency.getInstance("GBP");
        Money toConvertFrom = new Money(fromCurrency, new BigDecimal("0"));
        Currency toCurrency = Currency.getInstance("PLN");
        BigDecimal fromCurrencyToCurrencyRate = new BigDecimal("5.33");

        Mockito.when(exchangeRateResource.getExchangeRate(fromCurrency, toCurrency))
                .thenReturn(fromCurrencyToCurrencyRate);

        //when
        Money calculated = moneyConverter.convert(toConvertFrom, toCurrency);

        //then
        Assertions.assertThat(calculated).isEqualTo(new Money(toCurrency, new BigDecimal("0.00")));
    }

    @Test
    public void shouldReturnCorrectAmountIfBigInput(){

        //given
        Currency fromCurrency = Currency.getInstance("GBP");
        Money toConvertFrom = new Money(fromCurrency, new BigDecimal(Integer.MAX_VALUE));
        Currency toCurrency = Currency.getInstance("PLN");
        BigDecimal fromCurrencyToCurrencyRate = new BigDecimal("5.33");

        Mockito.when(exchangeRateResource.getExchangeRate(fromCurrency, toCurrency))
                .thenReturn(fromCurrencyToCurrencyRate);

        //when
        Money calculated = moneyConverter.convert(toConvertFrom, toCurrency);

        //then
        Assertions.assertThat(calculated).isEqualTo(new Money(toCurrency, new BigDecimal("11446087838.51")));

    }

    @Test
    public void shouldRoundDownCorrectly(){
        //given
        Currency fromCurrency = Currency.getInstance("GBP");
        Money toConvertFrom = new Money(fromCurrency, new BigDecimal("4.53"));
        Currency toCurrency = Currency.getInstance("PLN");
        BigDecimal fromCurrencyToCurrencyRate = new BigDecimal("5.33");

        Mockito.when(exchangeRateResource.getExchangeRate(fromCurrency, toCurrency))
                .thenReturn(fromCurrencyToCurrencyRate);

        //when
        Money calculated = moneyConverter.convert(toConvertFrom, toCurrency);

        //then
        Assertions.assertThat(calculated).isEqualTo(new Money(toCurrency, new BigDecimal("24.14")));
    }

    @Test
    public void shouldRoundUpCorrectly(){
        //given
        Currency fromCurrency = Currency.getInstance("GBP");
        Money toConvertFrom = new Money(fromCurrency, new BigDecimal("3.42"));
        Currency toCurrency = Currency.getInstance("PLN");
        BigDecimal fromCurrencyToCurrencyRate = new BigDecimal("5.33");

        Mockito.when(exchangeRateResource.getExchangeRate(fromCurrency, toCurrency))
                .thenReturn(fromCurrencyToCurrencyRate);

        //when
        Money calculated = moneyConverter.convert(toConvertFrom, toCurrency);

        //then
        Assertions.assertThat(calculated).isEqualTo(new Money(toCurrency, new BigDecimal("18.23")));
    }

    @Test
    public void shouldReturnSameValueIfSameCurrencyOnInput(){
        //given
        Currency fromCurrency = Currency.getInstance("GBP");
        Money toConvertFrom = new Money(fromCurrency, new BigDecimal("3.42"));
        Currency toCurrency = Currency.getInstance("GBP");
        BigDecimal fromCurrencyToCurrencyRate = new BigDecimal("1.00");

        Mockito.when(exchangeRateResource.getExchangeRate(fromCurrency, toCurrency))
                .thenReturn(fromCurrencyToCurrencyRate);

        //when
        Money calculated = moneyConverter.convert(toConvertFrom, toCurrency);

        //then
        Assertions.assertThat(calculated).isEqualTo(new Money(toCurrency, new BigDecimal("3.42")));
    }

}
