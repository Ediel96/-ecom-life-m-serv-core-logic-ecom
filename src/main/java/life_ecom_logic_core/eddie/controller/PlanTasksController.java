package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.PlanTasksApiDelegate;
import com.backend.organize_life.model.PlanTask;
import com.backend.organize_life.model.PlanTaskCreate;
import com.backend.organize_life.model.PlanTaskUpdate;
import life_ecom_logic_core.eddie.service.PlanTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanTasksController implements PlanTasksApiDelegate {

    private final PlanTaskService planTaskService;

    @Override
    public ResponseEntity<List<PlanTask>> planTasksGet(Integer planId) {
        log.debug("Listing plan tasks - planId: {}", planId);
        return ResponseEntity.ok(planTaskService.list(planId));
    }

    @Override
    public ResponseEntity<PlanTask> planTasksIdGet(Integer id) {
        log.debug("Fetching plan task with id: {}", id);
        PlanTask task = planTaskService.get(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @Override
    public ResponseEntity<PlanTask> planTasksPost(PlanTaskCreate planTaskCreate) {
        log.info("Creating new plan task");
        PlanTask created = planTaskService.create(planTaskCreate);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<PlanTask> planTasksIdPut(Integer id, PlanTaskUpdate planTaskUpdate) {
        log.info("Updating plan task with id: {}", id);
        PlanTask updated = planTaskService.update(id, planTaskUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> planTasksIdDelete(Integer id) {
        log.info("Deleting plan task with id: {}", id);
        if (planTaskService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
