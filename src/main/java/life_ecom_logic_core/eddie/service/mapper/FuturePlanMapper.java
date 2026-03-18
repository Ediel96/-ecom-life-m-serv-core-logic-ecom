package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.FuturePlan;
import com.backend.organize_life.model.FuturePlanCreate;
import com.backend.organize_life.model.FuturePlanStatus;
import com.backend.organize_life.model.FuturePlanUpdate;
import com.backend.organize_life.model.Priority;
import life_ecom_logic_core.eddie.domain.FuturePlanEntity;
import life_ecom_logic_core.eddie.domain.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
public class FuturePlanMapper {

    public FuturePlanEntity toEntity(FuturePlanCreate src) {
        FuturePlanEntity entity = new FuturePlanEntity();
        entity.setId(null);

        if (src.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(src.getUserId());
            entity.setUser(user);
        }

        entity.setTitle(src.getTitle());
        entity.setDescription(src.getDescription());
        entity.setTargetAmount(src.getTargetAmount() != null
                ? BigDecimal.valueOf(src.getTargetAmount())
                : BigDecimal.ZERO);
        entity.setTargetDate(src.getTargetDate());
        entity.setPriority(src.getPriority());
        entity.setStatus(src.getStatus() != null ? src.getStatus() : "ACTIVE");
        entity.setIcon(src.getIcon());
        entity.setReminderType(src.getReminderType());
        entity.setReminderDay(src.getReminderDay());

        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());

        return entity;
    }

    public FuturePlan toDto(FuturePlanEntity entity) {
        FuturePlan dto = new FuturePlan();
        dto.setId(entity.getId());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());

        if (entity.getTargetAmount() != null) {
            dto.setTargetAmount(entity.getTargetAmount().doubleValue());
        }

        dto.setTargetDate(entity.getTargetDate());

        if (entity.getPriority() != null) {
            try {
                dto.setPriority(Priority.valueOf(entity.getPriority().toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }

        if (entity.getStatus() != null) {
            try {
                dto.setStatus(FuturePlanStatus.valueOf(entity.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }

        dto.setIcon(entity.getIcon());
        dto.setReminderType(entity.getReminderType());
        dto.setReminderDay(entity.getReminderDay());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public FuturePlanEntity updateEntity(FuturePlanUpdate update, FuturePlanEntity entity) {
        if (update == null || entity == null) return entity;

        if (update.getUserId() != null) {
            UserEntity user = entity.getUser() != null ? entity.getUser() : new UserEntity();
            user.setId(update.getUserId());
            entity.setUser(user);
        }
        if (update.getTitle() != null)        entity.setTitle(update.getTitle());
        if (update.getDescription() != null)  entity.setDescription(update.getDescription());
        if (update.getTargetAmount() != null) entity.setTargetAmount(BigDecimal.valueOf(update.getTargetAmount()));
        if (update.getTargetDate() != null)   entity.setTargetDate(update.getTargetDate());
        if (update.getPriority() != null)     entity.setPriority(update.getPriority());
        if (update.getStatus() != null)       entity.setStatus(update.getStatus());
        if (update.getIcon() != null)         entity.setIcon(update.getIcon());
        if (update.getReminderType() != null) entity.setReminderType(update.getReminderType());
        if (update.getReminderDay() != null)  entity.setReminderDay(update.getReminderDay());

        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }
}
