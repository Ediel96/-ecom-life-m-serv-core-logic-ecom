package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.FrequencyType;
import com.backend.organize_life.model.RecurringTransaction;
import com.backend.organize_life.model.RecurringTransactionCreate;
import com.backend.organize_life.model.RecurringTransactionUpdate;
import life_ecom_logic_core.eddie.domain.RecurringTransactionEntity;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.domain.UserEntity;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class RecurringTransactionMapper {

    // ── Entity → DTO ──────────────────────────────────────────────────────────

    public RecurringTransaction toDto(RecurringTransactionEntity entity) {
        RecurringTransaction dto = new RecurringTransaction();
        dto.setId(entity.getId());
        dto.setTransactionId(entity.getTransaction() != null ? entity.getTransaction().getId() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
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

        if (src.getTransactionId() != null) {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setId(src.getTransactionId());
            entity.setTransaction(transaction);
        }

        if (src.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(src.getUserId());
            entity.setUser(user);
        }

        entity.setFrequency(mapFrequencyToDomain(src.getFrequency()));
        entity.setIntervalCount(src.getIntervalCount() != null ? src.getIntervalCount() : 1);
        entity.setStartDate(src.getStartDate());
        entity.setNextDueDate(src.getNextDueDate());
        entity.setNotificationDaysBefore(src.getNotificationDaysBefore() != null ? src.getNotificationDaysBefore() : 1);
        entity.setActive(Boolean.TRUE.equals(src.getIsActive() != null ? src.getIsActive() : Boolean.TRUE));
        entity.setLifestyle(Boolean.TRUE.equals(src.getIsLifestyle()));

        // Nullable endDate
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
            if (update.getEndDate().isPresent()) {
                entity.setEndDate(update.getEndDate().get());
            } else {
                entity.setEndDate(null);
            }
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

    /** Domain FrequencyType (lowercase) → API FrequencyType (uppercase). */
    private FrequencyType mapFrequencyToApi(life_ecom_logic_core.eddie.domain.FrequencyType domain) {
        if (domain == null) return null;
        return FrequencyType.valueOf(domain.name().toUpperCase(java.util.Locale.ROOT));
    }

    /** API FrequencyType (uppercase) → Domain FrequencyType (lowercase). */
    private life_ecom_logic_core.eddie.domain.FrequencyType mapFrequencyToDomain(FrequencyType api) {
        if (api == null) return null;
        return life_ecom_logic_core.eddie.domain.FrequencyType.valueOf(
                api.name().toLowerCase(java.util.Locale.ROOT));
    }
}
