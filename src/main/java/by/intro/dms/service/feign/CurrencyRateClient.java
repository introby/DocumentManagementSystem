package by.intro.dms.service.feign;

import by.intro.dms.model.valute.GeneralInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "CR", url = "${feign.client.url}")
public interface CurrencyRateClient {

    @GetMapping
    GeneralInfo findAll();

}
