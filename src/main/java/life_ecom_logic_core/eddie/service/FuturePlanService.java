package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.FuturePlan;
import com.backend.organize_life.model.FuturePlanCreate;
import com.backend.organize_life.model.FuturePlanStatus;
import com.backend.organize_life.model.FuturePlanUpdate;
import com.backend.organize_life.model.PlanProgress;

import java.util.List;
import java.util.UUID;

public interface FuturePlanService {

    List<FuturePlan> list(UUID userId, String status);

    FuturePlan get(Integer id);

    FuturePlan create(FuturePlanCreate create);

    FuturePlan update(Integer id, FuturePlanUpdate update);

    boolean delete(Integer id);

    List<PlanProgress> progress(UUID userId, FuturePlanStatus status);
}
