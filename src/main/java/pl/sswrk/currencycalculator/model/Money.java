package pl.sswrk.currencycalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@ToString
@AllArgsConstructor
public class Money {

    private final Currency currency;
    private final BigDecimal amount;

}
