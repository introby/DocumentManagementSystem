package by.intro.dms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    public static Map<String, Float> getRates() throws IOException {
        HashMap<String, Float> map = new HashMap<>();
        URL url = new URL("https://www.cbr-xml-daily.ru/daily_json.js");

        JsonNode rateNode = new ObjectMapper().readTree(url);

        float bynRate = rateNode.findValue("BYN").get("Value").floatValue();
        map.put("BYN", bynRate);
        float usdRate = rateNode.findValue("USD").get("Value").floatValue();
        map.put("USD", usdRate);
        float eurRate = rateNode.findValue("EUR").get("Value").floatValue();
        map.put("EUR", eurRate);

        return map;
    }
}