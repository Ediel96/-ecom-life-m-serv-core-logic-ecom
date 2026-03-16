package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.PlanTask;
import com.backend.organize_life.model.PlanTaskCreate;
import com.backend.organize_life.model.PlanTaskUpdate;
import life_ecom_logic_core.eddie.domain.PlanTaskEntity;
import life_ecom_logic_core.eddie.repository.PlanTaskRepository;
import life_ecom_logic_core.eddie.service.PlanTaskService;
import life_ecom_logic_core.eddie.service.mapper.PlanTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanTaskServiceImpl implements PlanTaskService {

    private final PlanTaskRepository repository;
    private final PlanTaskMapper mapper;

    @Override
    public List<PlanTask> list(Integer planId) {
        List<PlanTaskEntity> entities = planId != null
                ? repository.findByPlanId(planId)
                : repository.findAll();
        return entities.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PlanTask get(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("PlanTask not found: " + id));
    }

    @Override
    public PlanTask create(PlanTaskCreate create) {
        PlanTaskEntity entity = mapper.toEntity(create);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PlanTask update(Integer id, PlanTaskUpdate update) {
        PlanTaskEntity entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("PlanTask not found: " + id));
        mapper.updateEntity(update, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
