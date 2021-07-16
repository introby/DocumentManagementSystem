package by.intro.dms.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "CR", url = "${feign.client.url}")
public interface CurrencyRateClient {

    @GetMapping
    Object findAll();

}
