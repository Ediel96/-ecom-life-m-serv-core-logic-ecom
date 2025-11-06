// java
package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.domain.AccountEntity;
import life_ecom_logic_core.eddie.domain.CategoryEntity;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.domain.UserEntity;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
public class TransactionsMapper {

    public TransactionEntity toEntity(TransactionCreate src) {
        TransactionEntity dto = new TransactionEntity();
        dto.setId(null);

        // Account
        AccountEntity account = new AccountEntity();
        if (src.getAccountId() != null && src.getAccountId().isPresent()) {
            account.setId(src.getAccountId().get().longValue());
        }
        dto.setAccount(account);

        // User
        if (src.getUserId() != null && src.getUserId().isPresent()) {
            UserEntity user = dto.getUser() != null ? dto.getUser() : new UserEntity();
            user.setId(src.getUserId().get());
            dto.setUser(user);
        }

        // Category
        if (src.getCategoryId() != null && src.getCategoryId().isPresent()) {
            CategoryEntity category = dto.getCategory() != null ? dto.getCategory() : new CategoryEntity();
            category.setId(src.getCategoryId().get());
            dto.setCategory(category);
        }

        // Amount
        if (src.getAmount() != null) {
            dto.setAmount(java.math.BigDecimal.valueOf(src.getAmount()));
        } else {
            dto.setAmount(null);
        }

        // Transaction Type (API -> domain)
        if (src.getTransactionType() != null) {
            dto.setTransactionType(life_ecom_logic_core.eddie.domain.TransactionTypeEnum.valueOf(src.getTransactionType().name().toLowerCase()));
        }

        // Description
        if (src.getDescription() != null) {
            dto.setDescription(src.getDescription());
        }

        // Date
        if (src.getDate() != null) {
            dto.setDate(src.getDate());
        }

        // Timestamps
        dto.setCreatedAt(java.time.OffsetDateTime.now());
        dto.setUpdatedAt(java.time.OffsetDateTime.now());

        return dto;
    }

    public Transaction toDto(TransactionEntity entity) {
        Transaction transaction = new Transaction();
        transaction.setId(entity.getId());
        transaction.setAccountId(entity.getAccount() != null && entity.getAccount().getId() != null
                ? JsonNullable.of(entity.getAccount().getId().intValue())
                : JsonNullable.<Integer>undefined());
        transaction.setUserId(entity.getUser() != null && entity.getUser().getId() != null
                ? JsonNullable.of(entity.getUser().getId())
                : JsonNullable.<java.util.UUID>undefined());
        transaction.setCategoryId(entity.getCategory() != null && entity.getCategory().getId() != null
                ? JsonNullable.of(entity.getCategory().getId())
                : JsonNullable.<Integer>undefined());
        transaction.setAmount(entity.getAmount() != null ? entity.getAmount().doubleValue() : null);
        transaction.setTransactionType(
                entity.getTransactionType() != null
                        ? TransactionType.valueOf(entity.getTransactionType().name().toUpperCase())
                        : null
        );
        transaction.setDescription(entity.getDescription());
        transaction.setDate(entity.getDate());
        transaction.setCreatedAt(entity.getCreatedAt());
        transaction.setUpdatedAt(entity.getUpdatedAt());

        return transaction;
    }

    public TransactionUpdate toUpdate(TransactionEntity dto) {
        TransactionUpdate upd = new TransactionUpdate();

        if (dto.getAccount() != null && dto.getAccount().getId() != null) {
            upd.setAccountId(JsonNullable.of(dto.getAccount().getId().intValue()));
        }

        if (dto.getUser() != null && dto.getUser().getId() != null) {
            upd.setUserId(JsonNullable.of(dto.getUser().getId()));
        }

        if (dto.getCategory() != null && dto.getCategory().getId() != null) {
            upd.setCategoryId(JsonNullable.of(dto.getCategory().getId().intValue()));
        }

        if (dto.getAmount() != null) {
            upd.setAmount(dto.getAmount().doubleValue());
        }

        if (dto.getTransactionType() != null) {
            upd.setTransactionType(TransactionType.valueOf(dto.getTransactionType().name().toUpperCase()));
        }

        if (dto.getDescription() != null) {
            upd.setDescription(dto.getDescription());
        }

        if (dto.getDate() != null) {
            upd.setDate(dto.getDate());
        }

        return upd;
    }

    public TransactionEntity updateEntity(TransactionUpdate update, TransactionEntity entity) {
        if (update == null || entity == null) {
            return entity;
        }
        if (update.getAccountId() != null && update.getAccountId().isPresent()) {
            AccountEntity account = entity.getAccount() != null ? entity.getAccount() : new AccountEntity();
            account.setId(update.getAccountId().get().longValue());
            entity.setAccount(account);
        }
        if (update.getUserId() != null && update.getUserId().isPresent()) {
            UserEntity user = entity.getUser() != null ?  entity.getUser() : new UserEntity();
            user.setId(update.getUserId().get());
            entity.setUser(user);
        }
        if (update.getCategoryId() != null && update.getCategoryId().isPresent()) {
            CategoryEntity category = entity.getCategory() != null ? entity.getCategory() : new CategoryEntity();
            category.setId(update.getCategoryId().get());
            entity.setCategory(category);
        }
        if (update.getAmount() != null) {
            entity.setAmount(java.math.BigDecimal.valueOf(update.getAmount()));
        }
        if (update.getTransactionType() != null) {
            entity.setTransactionType(life_ecom_logic_core.eddie.domain.TransactionTypeEnum.valueOf(update.getTransactionType().name().toLowerCase()));
        }
        if (update.getDescription() != null) {
            entity.setDescription(update.getDescription());
        }
        if (update.getDate() != null) {
            entity.setDate(update.getDate());
        }
        entity.setUpdatedAt(java.time.OffsetDateTime.now());
        return entity;
    }
}
