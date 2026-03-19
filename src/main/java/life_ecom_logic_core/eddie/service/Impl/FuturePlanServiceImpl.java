package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.FuturePlan;
import com.backend.organize_life.model.FuturePlanCreate;
import com.backend.organize_life.model.FuturePlanStatus;
import com.backend.organize_life.model.FuturePlanUpdate;
import com.backend.organize_life.model.PlanProgress;
import life_ecom_logic_core.eddie.repository.FuturePlanRepository;
import life_ecom_logic_core.eddie.repository.projection.PlanProgressRow;
import life_ecom_logic_core.eddie.service.FuturePlanService;
import life_ecom_logic_core.eddie.service.mapper.FuturePlanMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturePlanServiceImpl implements FuturePlanService {

    private final FuturePlanRepository futurePlanRepository;
    private final FuturePlanMapper futurePlanMapper;

    @Override
    public List<FuturePlan> list(UUID userId, String status) {
        log.debug("Listing future plans - userId: {}, status: {}", userId, status);
        return futurePlanRepository.findByFilters(userId, status)
                .stream()
                .map(futurePlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FuturePlan get(Integer id) {
        log.debug("Fetching future plan with id: {}", id);
        FuturePlan result = futurePlanRepository.findById(id)
                .map(futurePlanMapper::toDto)
                .orElse(null);
        if (result == null) log.warn("Future plan not found with id: {}", id);
        return result;
    }

    @Override
    public FuturePlan create(FuturePlanCreate create) {
        log.info("Creating new future plan for userId: {}", create.getUserId());
        FuturePlan saved = futurePlanMapper.toDto(
                futurePlanRepository.save(futurePlanMapper.toEntity(create)));
        log.info("Future plan created with id: {}", saved.getId());
        return saved;
    }

    @Override
    public FuturePlan update(Integer id, FuturePlanUpdate update) {
        log.info("Updating future plan with id: {}", id);
        FuturePlan result = futurePlanRepository.findById(id)
                .map(entity -> {
                    FuturePlan saved = futurePlanMapper.toDto(
                            futurePlanRepository.save(futurePlanMapper.updateEntity(update, entity)));
                    log.info("Future plan updated id: {}", id);
                    return saved;
                })
                .orElse(null);
        if (result == null) log.warn("Future plan not found for update, id: {}", id);
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        log.info("Deleting future plan with id: {}", id);
        if (!futurePlanRepository.existsById(id)) {
            log.warn("Future plan not found for delete, id: {}", id);
            return false;
        }
        futurePlanRepository.deleteById(id);
        log.info("Future plan deleted id: {}", id);
        return true;
    }

    @Override
    public List<PlanProgress> progress(UUID userId, FuturePlanStatus status) {
        log.debug("Fetching plan progress - userId: {}, status: {}", userId, status);
        String userIdStr = userId != null ? userId.toString() : null;
        String statusStr = status != null ? status.getValue() : null;
        return futurePlanRepository.findProgress(userIdStr, statusStr)
                .stream()
                .map(this::toProgressDto)
                .collect(Collectors.toList());
    }

    private PlanProgress toProgressDto(PlanProgressRow row) {
        PlanProgress dto = new PlanProgress();
        dto.setId(row.getId());
        dto.setUserId(row.getUserId());
        dto.setTitle(row.getTitle());
        dto.setIcon(row.getIcon());
        dto.setTargetAmount(row.getTargetAmount() != null ? row.getTargetAmount().doubleValue() : 0.0);
        dto.setTargetDate(row.getTargetDate());
        dto.setReminderType(row.getReminderType());
        dto.setReminderDay(row.getReminderDay());
        dto.setSaved(row.getSaved() != null ? row.getSaved().doubleValue() : 0.0);
        dto.setProgressPct(row.getProgressPct() != null ? row.getProgressPct().doubleValue() : 0.0);
        dto.setAvgMonthlySavings(row.getAvgMonthlySavings() != null ? row.getAvgMonthlySavings().doubleValue() : 0.0);
        dto.setEstimatedCompletionDate(row.getEstimatedCompletionDate());
        if (row.getStatus() != null) {
            try {
                dto.setStatus(FuturePlanStatus.valueOf(row.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
        return dto;
    }
}
