package pl.sswrk.currencycalculator.gui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sswrk.currencycalculator.model.Money;
import pl.sswrk.currencycalculator.services.MoneyConverter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("exchange")
public class CurrencyExchangeGui extends VerticalLayout {


    public CurrencyExchangeGui(@Autowired MoneyConverter moneyConverter){

        HorizontalLayout moneyFrom;
        BigDecimalField moneyAmountFrom;
        ComboBox<String> currencyDropdownFrom;

        HorizontalLayout moneyTo;
        BigDecimalField moneyAmountTo;
        ComboBox<String> currencyDropdownTo;


        moneyFrom = new HorizontalLayout();

        moneyAmountFrom = new BigDecimalField("Select amount");
        currencyDropdownFrom = new ComboBox<>("From currency", Stream.of(AvailableCurrency.values())
                .map(AvailableCurrency::name)
                .collect(Collectors.toList()));
        currencyDropdownFrom.setValue(AvailableCurrency.GBP.toString());

        moneyFrom.add(moneyAmountFrom, currencyDropdownFrom);

        moneyTo = new HorizontalLayout();


        moneyAmountTo = new BigDecimalField("Calculated amount");
        currencyDropdownTo = new ComboBox<>("To currency", Stream.of(AvailableCurrency.values())
                .map(AvailableCurrency::name)
                .collect(Collectors.toList()));
        currencyDropdownTo.setValue(AvailableCurrency.PLN.toString());

        moneyTo.add(moneyAmountTo, currencyDropdownTo);


        currencyDropdownFrom.addValueChangeListener(event -> {
            Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());
            BigDecimal fromAmount = moneyAmountFrom.getValue();

            Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());

            moneyAmountTo.setValue(moneyConverter.convert(new Money(fromCurrency, fromAmount), toCurrency).getAmount());
        });

        moneyAmountFrom.setValueChangeMode(ValueChangeMode.LAZY);
        moneyAmountFrom.addValueChangeListener(event -> {
            if(event.isFromClient()) {
                Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());
                BigDecimal fromAmount = moneyAmountFrom.getValue();

                Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());

                moneyAmountTo.setValue(moneyConverter.convert(new Money(fromCurrency, fromAmount), toCurrency).getAmount());
            }
        });


        currencyDropdownTo.addValueChangeListener(event -> {
            Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());

            Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());
            BigDecimal toAmount = moneyAmountTo.getValue();

            moneyAmountFrom.setValue(moneyConverter.convert(new Money(toCurrency, toAmount), fromCurrency).getAmount());
        });

        moneyAmountTo.setValueChangeMode(ValueChangeMode.LAZY);
        moneyAmountTo.addValueChangeListener(event -> {
            if(event.isFromClient()) {
                Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());

                Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());
                BigDecimal toAmount = moneyAmountTo.getValue();

                moneyAmountFrom.setValue(moneyConverter.convert(new Money(toCurrency, toAmount), fromCurrency).getAmount());
            }
        });

        add(moneyFrom, moneyTo);
    }
}
