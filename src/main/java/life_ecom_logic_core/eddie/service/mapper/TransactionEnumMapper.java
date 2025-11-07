package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.TransactionTypeEnum;

public class TransactionEnumMapper {


    public TransactionType mapToDto(TransactionTypeEnum transactionType) {
        return switch (transactionType) {
            case income -> TransactionType.INCOME;
            case expense -> TransactionType.EXPENSE;
        };
    }
    public TransactionTypeEnum mapToEntityEnum(TransactionType transactionType) {
        return switch (transactionType) {
            case INCOME -> TransactionTypeEnum.income;
            case EXPENSE -> TransactionTypeEnum.expense;
        };
    }


}
