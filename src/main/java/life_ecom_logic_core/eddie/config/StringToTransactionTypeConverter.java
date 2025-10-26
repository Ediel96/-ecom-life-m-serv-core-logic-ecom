package life_ecom_logic_core.eddie.config;

import com.backend.organize_life.model.TransactionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTransactionTypeConverter implements Converter<String, TransactionType> {

    @Override
    public TransactionType convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return TransactionType.valueOf(source.toUpperCase());
    }
}