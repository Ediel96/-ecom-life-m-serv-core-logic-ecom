package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.FrequencyType;
import com.backend.organize_life.model.RecurringTransaction;
import com.backend.organize_life.model.RecurringTransactionCreate;
import com.backend.organize_life.model.RecurringTransactionUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.AccountEntity;
import life_ecom_logic_core.eddie.domain.CategoryEntity;
import life_ecom_logic_core.eddie.domain.RecurringTransactionEntity;
import life_ecom_logic_core.eddie.domain.TransactionTypeEnum;
import life_ecom_logic_core.eddie.domain.UserEntity;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
public class RecurringTransactionMapper {

    // ── Entity → DTO ──────────────────────────────────────────────────────────

    public RecurringTransaction toDto(RecurringTransactionEntity entity) {
        RecurringTransaction dto = new RecurringTransaction();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);

        // Template data
        dto.setAccountId(entity.getAccount() != null
                ? JsonNullable.of(entity.getAccount().getId())
                : JsonNullable.undefined());
        dto.setCategoryId(entity.getCategory() != null
                ? JsonNullable.of(entity.getCategory().getId())
                : JsonNullable.undefined());
        dto.setAmount(entity.getAmount() != null ? entity.getAmount().doubleValue() : null);
        dto.setDescription(entity.getDescription());
        dto.setTransactionType(mapTransactionTypeToApi(entity.getTransactionType()));

        // Schedule config
        dto.setFrequency(mapFrequencyToApi(entity.getFrequency()));
        dto.setIntervalCount(entity.getIntervalCount());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate() != null
                ? JsonNullable.of(entity.getEndDate())
                : JsonNullable.undefined());
        dto.setNextDueDate(entity.getNextDueDate());
        dto.setLastProcessedDate(entity.getLastProcessedDate() != null
                ? JsonNullable.of(entity.getLastProcessedDate())
                : JsonNullable.undefined());
        dto.setNotificationDaysBefore(entity.getNotificationDaysBefore());
        dto.setIsActive(entity.isActive());
        dto.setIsLifestyle(entity.isLifestyle());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // ── Create DTO → Entity ───────────────────────────────────────────────────

    public RecurringTransactionEntity toEntity(RecurringTransactionCreate src) {
        RecurringTransactionEntity entity = new RecurringTransactionEntity();

        if (src.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(src.getUserId());
            entity.setUser(user);
        }

        // Template data
        if (src.getAccountId() != null && src.getAccountId().isPresent() && src.getAccountId().get() != null) {
            AccountEntity account = new AccountEntity();
            account.setId(src.getAccountId().get());
            entity.setAccount(account);
        }
        if (src.getCategoryId() != null && src.getCategoryId().isPresent()) {
            CategoryEntity category = new CategoryEntity();
            category.setId(src.getCategoryId().get());
            entity.setCategory(category);
        }
        if (src.getAmount() != null) {
            entity.setAmount(BigDecimal.valueOf(src.getAmount()));
        }
        entity.setDescription(src.getDescription());
        if (src.getTransactionType() != null) {
            entity.setTransactionType(mapTransactionTypeToDomain(src.getTransactionType()));
        }

        // Schedule config
        entity.setFrequency(mapFrequencyToDomain(src.getFrequency()));
        entity.setIntervalCount(src.getIntervalCount() != null ? src.getIntervalCount() : 1);
        entity.setStartDate(src.getStartDate());
        entity.setNextDueDate(src.getNextDueDate());
        entity.setNotificationDaysBefore(src.getNotificationDaysBefore() != null ? src.getNotificationDaysBefore() : 1);
        entity.setActive(src.getIsActive() == null || Boolean.TRUE.equals(src.getIsActive()));
        entity.setLifestyle(Boolean.TRUE.equals(src.getIsLifestyle()));

        if (src.getEndDate() != null && src.getEndDate().isPresent()) {
            entity.setEndDate(src.getEndDate().get());
        }

        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

    // ── Update DTO → Entity (partial) ─────────────────────────────────────────

    public RecurringTransactionEntity updateEntity(RecurringTransactionUpdate update, RecurringTransactionEntity entity) {
        if (update == null || entity == null) return entity;

        // Template data
        if (update.getAccountId() != null && update.getAccountId().isPresent() && update.getAccountId().get() != null) {
            AccountEntity account = entity.getAccount() != null ? entity.getAccount() : new AccountEntity();
            account.setId(update.getAccountId().get());
            entity.setAccount(account);
        }
        if (update.getCategoryId() != null && update.getCategoryId().isPresent()) {
            CategoryEntity category = entity.getCategory() != null ? entity.getCategory() : new CategoryEntity();
            category.setId(update.getCategoryId().get());
            entity.setCategory(category);
        }
        if (update.getAmount() != null) {
            entity.setAmount(BigDecimal.valueOf(update.getAmount()));
        }
        if (update.getDescription() != null) {
            entity.setDescription(update.getDescription());
        }
        if (update.getTransactionType() != null) {
            entity.setTransactionType(mapTransactionTypeToDomain(update.getTransactionType()));
        }

        // Schedule config
        if (update.getFrequency() != null) {
            entity.setFrequency(mapFrequencyToDomain(update.getFrequency()));
        }
        if (update.getIntervalCount() != null) {
            entity.setIntervalCount(update.getIntervalCount());
        }
        if (update.getStartDate() != null) {
            entity.setStartDate(update.getStartDate());
        }
        if (update.getEndDate() != null) {
            entity.setEndDate(update.getEndDate().isPresent() ? update.getEndDate().get() : null);
        }
        if (update.getNextDueDate() != null) {
            entity.setNextDueDate(update.getNextDueDate());
        }
        if (update.getNotificationDaysBefore() != null) {
            entity.setNotificationDaysBefore(update.getNotificationDaysBefore());
        }
        if (update.getIsActive() != null) {
            entity.setActive(update.getIsActive());
        }
        if (update.getIsLifestyle() != null) {
            entity.setLifestyle(update.getIsLifestyle());
        }

        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

    // ── Enum helpers ──────────────────────────────────────────────────────────

    private FrequencyType mapFrequencyToApi(life_ecom_logic_core.eddie.domain.FrequencyType domain) {
        if (domain == null) return null;
        return FrequencyType.valueOf(domain.name().toUpperCase(java.util.Locale.ROOT));
    }

    private life_ecom_logic_core.eddie.domain.FrequencyType mapFrequencyToDomain(FrequencyType api) {
        if (api == null) return null;
        return life_ecom_logic_core.eddie.domain.FrequencyType.valueOf(
                api.name().toLowerCase(java.util.Locale.ROOT));
    }

    private TransactionType mapTransactionTypeToApi(TransactionTypeEnum domain) {
        if (domain == null) return null;
        return TransactionType.valueOf(domain.name().toUpperCase(java.util.Locale.ROOT));
    }

    private TransactionTypeEnum mapTransactionTypeToDomain(TransactionType api) {
        if (api == null) return null;
        return TransactionTypeEnum.valueOf(api.name().toLowerCase(java.util.Locale.ROOT));
    }
}
