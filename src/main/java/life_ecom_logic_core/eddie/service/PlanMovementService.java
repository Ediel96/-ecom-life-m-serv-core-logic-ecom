package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.PlanMovement;
import com.backend.organize_life.model.PlanMovementCreate;
import com.backend.organize_life.model.PlanMovementUpdate;

import java.util.List;

public interface PlanMovementService {

    List<PlanMovement> list(Integer planId);

    PlanMovement get(Integer id);

    PlanMovement create(PlanMovementCreate create);

    PlanMovement update(Integer id, PlanMovementUpdate update);

    boolean delete(Integer id);
}
