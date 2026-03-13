package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.PlanMovement;
import com.backend.organize_life.model.PlanMovementCreate;
import com.backend.organize_life.model.PlanMovementUpdate;
import life_ecom_logic_core.eddie.repository.PlanMovementRepository;
import life_ecom_logic_core.eddie.service.PlanMovementService;
import life_ecom_logic_core.eddie.service.mapper.PlanMovementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanMovementServiceImpl implements PlanMovementService {

    private final PlanMovementRepository planMovementRepository;
    private final PlanMovementMapper planMovementMapper;

    @Override
    public List<PlanMovement> list(Integer planId) {
        log.debug("Listing plan movements - planId: {}", planId);
        return planMovementRepository.findByFilters(planId)
                .stream()
                .map(planMovementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlanMovement get(Integer id) {
        log.debug("Fetching plan movement with id: {}", id);
        return planMovementRepository.findById(id)
                .map(planMovementMapper::toDto)
                .orElse(null);
    }

    @Override
    public PlanMovement create(PlanMovementCreate create) {
        log.info("Creating new plan movement");
        return planMovementMapper.toDto(
                planMovementRepository.save(planMovementMapper.toEntity(create)));
    }

    @Override
    public PlanMovement update(Integer id, PlanMovementUpdate update) {
        log.info("Updating plan movement with id: {}", id);
        return planMovementRepository.findById(id)
                .map(entity -> planMovementMapper.toDto(
                        planMovementRepository.save(planMovementMapper.updateEntity(update, entity))))
                .orElse(null);
    }

    @Override
    public boolean delete(Integer id) {
        log.info("Deleting plan movement with id: {}", id);
        if (!planMovementRepository.existsById(id)) {
            return false;
        }
        planMovementRepository.deleteById(id);
        return true;
    }
}
