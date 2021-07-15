package by.intro.dms.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "CR", url = "https://www.cbr-xml-daily.ru/daily_json.js")
public interface CurrencyRateClient {

    @GetMapping
    Map<?, ?> findAll();

}
