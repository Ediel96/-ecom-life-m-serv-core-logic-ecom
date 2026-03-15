package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.PlanMovement;
import com.backend.organize_life.model.PlanMovementCreate;
import com.backend.organize_life.model.PlanMovementType;
import com.backend.organize_life.model.PlanMovementUpdate;
import life_ecom_logic_core.eddie.domain.FuturePlanEntity;
import life_ecom_logic_core.eddie.domain.PlanMovementEntity;
import org.springframework.stereotype.Component;

@Component
public class PlanMovementMapper {

    public PlanMovementEntity toEntity(PlanMovementCreate src) {
        PlanMovementEntity entity = new PlanMovementEntity();
        entity.setId(null);

        if (src.getPlanId() != null) {
            FuturePlanEntity plan = new FuturePlanEntity();
            plan.setId(src.getPlanId());
            entity.setPlan(plan);
        }

        if (src.getAmount() != null) {
            entity.setAmount(java.math.BigDecimal.valueOf(src.getAmount()));
        }

        entity.setDate(src.getDate());
        entity.setNote(src.getNote());
        entity.setType(toBoolean(src.getType()));

        return entity;
    }

    public PlanMovement toDto(PlanMovementEntity entity) {
        PlanMovement dto = new PlanMovement();
        dto.setId(entity.getId());

        if (entity.getPlan() != null) {
            dto.setPlanId(entity.getPlan().getId());
        }

        if (entity.getAmount() != null) {
            dto.setAmount(entity.getAmount().doubleValue());
        }

        dto.setDate(entity.getDate());
        dto.setNote(entity.getNote());

        if (entity.getType() != null) {
            dto.setType(Boolean.TRUE.equals(entity.getType()) ? PlanMovementType.DEPOSIT : PlanMovementType.WITHDRAWAL);
        }

        return dto;
    }

    public PlanMovementEntity updateEntity(PlanMovementUpdate update, PlanMovementEntity entity) {
        if (update == null || entity == null) {
            return entity;
        }

        if (update.getPlanId() != null) {
            FuturePlanEntity plan = entity.getPlan() != null ? entity.getPlan() : new FuturePlanEntity();
            plan.setId(update.getPlanId());
            entity.setPlan(plan);
        }

        if (update.getAmount() != null) {
            entity.setAmount(java.math.BigDecimal.valueOf(update.getAmount()));
        }

        if (update.getDate() != null) {
            entity.setDate(update.getDate());
        }

        if (update.getNote() != null) {
            entity.setNote(update.getNote());
        }

        if (update.getType() != null) {
            entity.setType(toBoolean(update.getType()));
        }

        return entity;
    }

    /** PlanMovementType enum → Boolean stored in DB (DEPOSIT=true, WITHDRAWAL=false). */
    private Boolean toBoolean(PlanMovementType type) {
        if (type == null) return null;
        return type == PlanMovementType.DEPOSIT;
    }
}
