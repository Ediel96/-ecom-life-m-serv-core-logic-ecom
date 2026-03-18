package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.PlanTask;
import com.backend.organize_life.model.PlanTaskCreate;
import com.backend.organize_life.model.PlanTaskUpdate;
import life_ecom_logic_core.eddie.domain.FuturePlanEntity;
import life_ecom_logic_core.eddie.domain.PlanTaskEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
public class PlanTaskMapper {

    public PlanTaskEntity toEntity(PlanTaskCreate src) {
        PlanTaskEntity entity = new PlanTaskEntity();
        entity.setId(null);

        if (src.getPlanId() != null) {
            FuturePlanEntity plan = new FuturePlanEntity();
            plan.setId(src.getPlanId());
            entity.setPlan(plan);
        }

        entity.setTitle(src.getTitle());
        entity.setValue(src.getValue() != null ? BigDecimal.valueOf(src.getValue()) : BigDecimal.ZERO);
        entity.setCompleted(Boolean.TRUE.equals(src.getIsCompleted()));
        entity.setCreatedAt(OffsetDateTime.now());

        return entity;
    }

    public PlanTask toDto(PlanTaskEntity entity) {
        PlanTask dto = new PlanTask();
        dto.setId(entity.getId());

        if (entity.getPlan() != null) {
            dto.setPlanId(entity.getPlan().getId());
        }

        dto.setTitle(entity.getTitle());
        dto.setValue(entity.getValue() != null ? entity.getValue().doubleValue() : 0.0);
        dto.setIsCompleted(entity.isCompleted());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    public PlanTaskEntity updateEntity(PlanTaskUpdate update, PlanTaskEntity entity) {
        if (update == null || entity == null) return entity;

        if (update.getTitle() != null) {
            entity.setTitle(update.getTitle());
        }
        if (update.getValue() != null) {
            entity.setValue(BigDecimal.valueOf(update.getValue()));
        }
        if (update.getIsCompleted() != null) {
            entity.setCompleted(update.getIsCompleted());
        }

        return entity;
    }
}
