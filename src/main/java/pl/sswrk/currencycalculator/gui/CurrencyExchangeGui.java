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

    private final MoneyConverter moneyConverter;

    private HorizontalLayout moneyFrom;
    private BigDecimalField moneyAmountFrom;
    private ComboBox<String> currencyDropdownFrom;

    private HorizontalLayout moneyTo;
    private BigDecimalField moneyAmountTo;
    private ComboBox<String> currencyDropdownTo;

    public CurrencyExchangeGui(@Autowired MoneyConverter moneyConverter){

        this.moneyConverter = moneyConverter;

        initialize();
    }

    private void initialize() {

        createMoneyFields();
        setValueChangeListeners();

    }

    private void createMoneyFields() {
        createMoneyFrom();
        createMoneyTo();

        add(moneyFrom, moneyTo);
    }

    private void createMoneyFrom() {
        moneyFrom = new HorizontalLayout();

        moneyAmountFrom = new BigDecimalField("You send");
        currencyDropdownFrom = new ComboBox<>("Select currency", Stream.of(AvailableCurrency.values())
                .map(AvailableCurrency::name)
                .collect(Collectors.toList()));
        currencyDropdownFrom.setValue(AvailableCurrency.GBP.toString());

        moneyFrom.add(moneyAmountFrom, currencyDropdownFrom);
    }

    private void createMoneyTo() {
        moneyTo = new HorizontalLayout();

        moneyAmountTo = new BigDecimalField("They receive");
        currencyDropdownTo = new ComboBox<>("Select currency", Stream.of(AvailableCurrency.values())
                .map(AvailableCurrency::name)
                .collect(Collectors.toList()));
        currencyDropdownTo.setValue(AvailableCurrency.PLN.toString());

        moneyTo.add(moneyAmountTo, currencyDropdownTo);
    }

    private void setValueChangeListeners(){

        setMoneyFromValueChangeListeners();
        setMoneyToValueChangeListeners();

    }

    private void setMoneyToValueChangeListeners() {
        currencyDropdownTo.addValueChangeListener(event -> {
            Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());

            Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());
            BigDecimal toAmount = moneyAmountTo.getValue();

            moneyAmountFrom.setValue(moneyConverter.convert(new Money(toCurrency, toAmount), fromCurrency).getAmount());
        });

        moneyAmountTo.setValueChangeMode(ValueChangeMode.LAZY);
        moneyAmountTo.addValueChangeListener(event -> {
            if(event.isFromClient() && moneyAmountTo.getValue()!=null) {
                Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());

                Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());
                BigDecimal toAmount = moneyAmountTo.getValue();

                moneyAmountFrom.setValue(moneyConverter.convert(new Money(toCurrency, toAmount), fromCurrency).getAmount());
            }
        });
    }

    private void setMoneyFromValueChangeListeners() {
        currencyDropdownFrom.addValueChangeListener(event -> {
            Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());
            BigDecimal fromAmount = moneyAmountFrom.getValue();

            Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());

            moneyAmountTo.setValue(moneyConverter.convert(new Money(fromCurrency, fromAmount), toCurrency).getAmount());
        });

        moneyAmountFrom.setValueChangeMode(ValueChangeMode.LAZY);
        moneyAmountFrom.addValueChangeListener(event -> {
            if(event.isFromClient() && moneyAmountFrom.getValue()!=null) {
                Currency fromCurrency = Currency.getInstance(currencyDropdownFrom.getValue());
                BigDecimal fromAmount = moneyAmountFrom.getValue();

                Currency toCurrency = Currency.getInstance(currencyDropdownTo.getValue());

                moneyAmountTo.setValue(moneyConverter.convert(new Money(fromCurrency, fromAmount), toCurrency).getAmount());
            }
        });
    }
}
