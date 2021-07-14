package by.intro.dms.service.feign;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateClientBuilder {

    public CurrencyRateClient currencyRateClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .target(CurrencyRateClient.class, "https://www.cbr-xml-daily.ru/daily_json.js");
}
