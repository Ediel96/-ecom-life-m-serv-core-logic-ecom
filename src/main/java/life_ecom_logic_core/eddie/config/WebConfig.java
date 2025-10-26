package life_ecom_logic_core.eddie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToTransactionTypeConverter transactionTypeConverter;

    public WebConfig(StringToTransactionTypeConverter transactionTypeConverter) {
        this.transactionTypeConverter = transactionTypeConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(transactionTypeConverter);
    }
}