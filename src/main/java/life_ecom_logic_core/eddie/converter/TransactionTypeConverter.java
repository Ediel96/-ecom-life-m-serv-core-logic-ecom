package life_ecom_logic_core.eddie.converter;

import com.backend.organize_life.model.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import life_ecom_logic_core.eddie.domain.TransactionTypeEnum;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public TransactionType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TransactionType.valueOf(dbData.toUpperCase());
    }

}