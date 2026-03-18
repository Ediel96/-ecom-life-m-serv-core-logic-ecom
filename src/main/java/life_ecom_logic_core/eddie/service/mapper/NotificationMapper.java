package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.Notification;
import com.backend.organize_life.model.NotificationType;
import life_ecom_logic_core.eddie.domain.NotificationEntity;
import life_ecom_logic_core.eddie.domain.NotificationTypeEnum;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    // ── Entity → DTO ──────────────────────────────────────────────────────────

    public Notification toDto(NotificationEntity entity) {
        Notification dto = new Notification();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);

        dto.setRecurringTransactionId(
                entity.getRecurringTransaction() != null && entity.getRecurringTransaction().getId() != null
                        ? JsonNullable.of(entity.getRecurringTransaction().getId())
                        : JsonNullable.undefined());

        dto.setTransactionId(
                entity.getTransaction() != null && entity.getTransaction().getId() != null
                        ? JsonNullable.of(entity.getTransaction().getId())
                        : JsonNullable.undefined());

        dto.setType(mapTypeToApi(entity.getType()));
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setScheduledFor(entity.getScheduledFor());
        dto.setSentAt(entity.getSentAt() != null
                ? JsonNullable.of(entity.getSentAt())
                : JsonNullable.undefined());

        dto.setPlanId(entity.getPlan() != null && entity.getPlan().getId() != null
                ? JsonNullable.of(entity.getPlan().getId())
                : JsonNullable.undefined());

        dto.setIsRead(entity.isRead());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // ── Enum helpers ──────────────────────────────────────────────────────────

    private NotificationType mapTypeToApi(NotificationTypeEnum domain) {
        if (domain == null) return null;
        return switch (domain) {
            case payment_reminder  -> NotificationType.PAYMENT_REMINDER;
            case recurring_payment -> NotificationType.RECURRING_PAYMENT;
            case system            -> NotificationType.SYSTEM;
        };
    }

    public NotificationTypeEnum mapTypeToDomain(NotificationType api) {
        if (api == null) return null;
        return switch (api) {
            case PAYMENT_REMINDER  -> NotificationTypeEnum.payment_reminder;
            case RECURRING_PAYMENT -> NotificationTypeEnum.recurring_payment;
            case SYSTEM            -> NotificationTypeEnum.system;
        };
    }
}
