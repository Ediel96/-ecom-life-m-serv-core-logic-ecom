package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.PlanMovement;
import com.backend.organize_life.model.PlanMovementCreate;
import com.backend.organize_life.model.PlanMovementType;
import com.backend.organize_life.model.PlanMovementUpdate;
import life_ecom_logic_core.eddie.domain.FuturePlanEntity;
import life_ecom_logic_core.eddie.domain.PlanMovementEntity;
import life_ecom_logic_core.eddie.domain.PlanMovementTypeEnum;
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
        entity.setMovementType(toDomainEnum(src.getMovementType()));

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
        dto.setMovementType(toApiEnum(entity.getMovementType()));

        return dto;
    }

    public PlanMovementEntity updateEntity(PlanMovementUpdate update, PlanMovementEntity entity) {
        if (update == null || entity == null) return entity;

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
        if (update.getMovementType() != null) {
            entity.setMovementType(toDomainEnum(update.getMovementType()));
        }

        return entity;
    }

    private PlanMovementTypeEnum toDomainEnum(PlanMovementType api) {
        if (api == null) return PlanMovementTypeEnum.DEPOSIT;
        return switch (api) {
            case DEPOSIT  -> PlanMovementTypeEnum.DEPOSIT;
            case WITHDRAW -> PlanMovementTypeEnum.WITHDRAW;
        };
    }

    private PlanMovementType toApiEnum(PlanMovementTypeEnum domain) {
        if (domain == null) return null;
        return switch (domain) {
            case DEPOSIT  -> PlanMovementType.DEPOSIT;
            case WITHDRAW -> PlanMovementType.WITHDRAW;
        };
    }
}
