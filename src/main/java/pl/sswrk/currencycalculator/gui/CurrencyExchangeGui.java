package pl.sswrk.currencycalculator.gui;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sswrk.currencycalculator.services.MoneyConverter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("exchange")
public class CurrencyExchangeGui extends VerticalLayout {

    public CurrencyExchangeGui(@Autowired MoneyConverter moneyConverter){

        HorizontalLayout moneyFrom = new HorizontalLayout();

        TextField moneyAmountFrom = new TextField("Select amount");
        moneyAmountFrom.setValue("1.00");
        ComboBox<String> currencyDropdownFrom = new ComboBox<>("From currency", Stream.of(AvailableCurrency.values())
                .map(AvailableCurrency::name)
                .collect(Collectors.toList()));
        currencyDropdownFrom.setValue(AvailableCurrency.GBP.toString());



        moneyFrom.add(moneyAmountFrom);
        moneyFrom.add(currencyDropdownFrom);

        HorizontalLayout moneyTo = new HorizontalLayout();

        TextField moneyAmountTo = new TextField("Calculated amount");
        ComboBox<String> currencyDropdownTo = new ComboBox<>("To currency", Stream.of(AvailableCurrency.values())
                .map(AvailableCurrency::name)
                .collect(Collectors.toList()));
        currencyDropdownTo.setValue(AvailableCurrency.PLN.toString());

        moneyTo.add(moneyAmountTo);
        moneyTo.add(currencyDropdownTo);

        add(moneyFrom, moneyTo);
    }
}
