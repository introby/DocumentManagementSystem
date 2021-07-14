package by.intro.dms.service.feign;

import by.intro.dms.model.feign.GeneralInfo;
import feign.RequestLine;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyRateClient {

    @RequestLine("GET")
    GeneralInfo findAll();

}
