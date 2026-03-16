package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.PlanTask;
import com.backend.organize_life.model.PlanTaskCreate;
import com.backend.organize_life.model.PlanTaskUpdate;

import java.util.List;

public interface PlanTaskService {

    List<PlanTask> list(Integer planId);

    PlanTask get(Integer id);

    PlanTask create(PlanTaskCreate create);

    PlanTask update(Integer id, PlanTaskUpdate update);

    boolean delete(Integer id);
}
