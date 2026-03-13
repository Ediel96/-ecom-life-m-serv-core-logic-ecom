package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.PlanMovementsApiDelegate;
import com.backend.organize_life.model.PlanMovement;
import com.backend.organize_life.model.PlanMovementCreate;
import com.backend.organize_life.model.PlanMovementUpdate;
import life_ecom_logic_core.eddie.service.PlanMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanMovementsController implements PlanMovementsApiDelegate {

    private final PlanMovementService planMovementService;

    @Override
    public ResponseEntity<List<PlanMovement>> planMovementsGet(Integer planId) {
        log.debug("Listing plan movements - planId: {}", planId);
        return ResponseEntity.ok(planMovementService.list(planId));
    }

    @Override
    public ResponseEntity<PlanMovement> planMovementsIdGet(Integer id) {
        log.debug("Fetching plan movement with id: {}", id);
        PlanMovement movement = planMovementService.get(id);
        if (movement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movement);
    }

    @Override
    public ResponseEntity<PlanMovement> planMovementsPost(PlanMovementCreate planMovementCreate) {
        log.info("Creating new plan movement");
        PlanMovement created = planMovementService.create(planMovementCreate);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<PlanMovement> planMovementsIdPut(Integer id, PlanMovementUpdate planMovementUpdate) {
        log.info("Updating plan movement with id: {}", id);
        PlanMovement updated = planMovementService.update(id, planMovementUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> planMovementsIdDelete(Integer id) {
        log.info("Deleting plan movement with id: {}", id);
        if (planMovementService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
