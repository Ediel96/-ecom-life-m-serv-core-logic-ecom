package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.PlanTask;
import com.backend.organize_life.model.PlanTaskCreate;
import com.backend.organize_life.model.PlanTaskUpdate;
import life_ecom_logic_core.eddie.domain.PlanTaskEntity;
import life_ecom_logic_core.eddie.repository.PlanTaskRepository;
import life_ecom_logic_core.eddie.service.PlanTaskService;
import life_ecom_logic_core.eddie.service.mapper.PlanTaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanTaskServiceImpl implements PlanTaskService {

    private final PlanTaskRepository repository;
    private final PlanTaskMapper mapper;

    @Override
    public List<PlanTask> list(Integer planId) {
        log.debug("Listing plan tasks - planId: {}", planId);
        List<PlanTaskEntity> entities = planId != null
                ? repository.findByPlanId(planId)
                : repository.findAll();
        log.debug("Found {} plan tasks for planId: {}", entities.size(), planId);
        return entities.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PlanTask get(Integer id) {
        log.debug("Fetching plan task with id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.debug("Found plan task id: {}", id);
                    return mapper.toDto(entity);
                })
                .orElseThrow(() -> {
                    log.warn("Plan task not found with id: {}", id);
                    return new jakarta.persistence.EntityNotFoundException("PlanTask not found: " + id);
                });
    }

    @Override
    public PlanTask create(PlanTaskCreate create) {
        log.info("Creating plan task for planId: {}", create.getPlanId());
        PlanTaskEntity entity = mapper.toEntity(create);
        PlanTask saved = mapper.toDto(repository.save(entity));
        log.info("Plan task created with id: {}", saved.getId());
        return saved;
    }

    @Override
    public PlanTask update(Integer id, PlanTaskUpdate update) {
        log.info("Updating plan task with id: {}", id);
        PlanTaskEntity entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Plan task not found for update, id: {}", id);
                    return new jakarta.persistence.EntityNotFoundException("PlanTask not found: " + id);
                });
        mapper.updateEntity(update, entity);
        PlanTask saved = mapper.toDto(repository.save(entity));
        log.info("Plan task updated id: {}", id);
        return saved;
    }

    @Override
    public boolean delete(Integer id) {
        log.info("Deleting plan task with id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Plan task not found for delete, id: {}", id);
            return false;
        }
        repository.deleteById(id);
        log.info("Plan task deleted id: {}", id);
        return true;
    }
}
