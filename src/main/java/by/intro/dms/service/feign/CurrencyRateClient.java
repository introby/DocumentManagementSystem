package by.intro.dms.service.feign;

import by.intro.dms.model.valute.CurrencyApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "CR", url = "${feign.client.url}")
public interface CurrencyRateClient {

    @GetMapping
    CurrencyApiResponse findAll();

}
