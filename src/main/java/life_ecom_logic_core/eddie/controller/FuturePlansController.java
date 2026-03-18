package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.FuturePlansApiDelegate;
import com.backend.organize_life.model.FuturePlan;
import com.backend.organize_life.model.FuturePlanCreate;
import com.backend.organize_life.model.FuturePlanStatus;
import com.backend.organize_life.model.FuturePlanUpdate;
import com.backend.organize_life.model.PlanProgress;
import life_ecom_logic_core.eddie.service.FuturePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class FuturePlansController implements FuturePlansApiDelegate {

    private final FuturePlanService futurePlanService;

    @Override
    public ResponseEntity<List<FuturePlan>> futurePlansGet(UUID userId, String status) {
        log.debug("Listing future plans - userId: {}, status: {}", userId, status);
        return ResponseEntity.ok(futurePlanService.list(userId, status));
    }

    @Override
    public ResponseEntity<FuturePlan> futurePlansIdGet(Integer id) {
        log.debug("Fetching future plan with id: {}", id);
        FuturePlan plan = futurePlanService.get(id);
        if (plan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plan);
    }

    @Override
    public ResponseEntity<FuturePlan> futurePlansPost(FuturePlanCreate futurePlanCreate) {
        log.info("Creating new future plan");
        FuturePlan created = futurePlanService.create(futurePlanCreate);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<FuturePlan> futurePlansIdPut(Integer id, FuturePlanUpdate futurePlanUpdate) {
        log.info("Updating future plan with id: {}", id);
        FuturePlan updated = futurePlanService.update(id, futurePlanUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> futurePlansIdDelete(Integer id) {
        log.info("Deleting future plan with id: {}", id);
        if (futurePlanService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<PlanProgress>> futurePlansProgressGet(UUID userId, FuturePlanStatus status) {
        log.debug("Fetching plan progress - userId: {}, status: {}", userId, status);
        return ResponseEntity.ok(futurePlanService.progress(userId, status));
    }
}
