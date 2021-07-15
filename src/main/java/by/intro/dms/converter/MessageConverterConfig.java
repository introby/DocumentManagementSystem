package by.intro.dms.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConverterConfig {

    @Bean
    public JavaScriptMessageConverter abstractJackson2HttpMessageConverter() {
        return new JavaScriptMessageConverter();
    }
}
