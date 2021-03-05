package pl.sswrk.currencycalculator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Currency;

@Service
@AllArgsConstructor
public class NbpExchangeRateResource implements ExchangeRateResource{

    private final RestTemplate restTemplate;

    @Override
    public BigDecimal getExchangeRate(Currency from, Currency to) {

        BigDecimal exchangeRate = new BigDecimal("1.00");

        if(from.getCurrencyCode().equals(to.getCurrencyCode())){
            return exchangeRate;
        }
        if(!from.getCurrencyCode().equals("PLN")){
            String parsedExchangeRate = getNbpExchangeRateFromPln(from.getCurrencyCode());
            exchangeRate = exchangeRate.multiply(new BigDecimal(parsedExchangeRate));
        }
        if(!to.getCurrencyCode().equals("PLN")){
            String parsedExchangeRate = getNbpExchangeRateFromPln(to.getCurrencyCode());
            exchangeRate = exchangeRate.divide(new BigDecimal(parsedExchangeRate), RoundingMode.HALF_EVEN);
        }

        return exchangeRate;
    }

    private String getNbpExchangeRateFromPln(String currencyCode) {
        String apiUrlTemplate = "http://api.nbp.pl/api/exchangerates/rates/a/{0}/?format=json";
        String apiUrl = MessageFormat.format(apiUrlTemplate, currencyCode);

        String response = restTemplate.getForObject(apiUrl, String.class);
        String exchangeRate = null;
        try {
            JsonNode parent = new ObjectMapper().readValue(response, JsonNode.class);
            exchangeRate = parent.at("/rates/0/mid").toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        return exchangeRate;
    }
}
