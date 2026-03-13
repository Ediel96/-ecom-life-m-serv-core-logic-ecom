package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.FuturePlan;
import com.backend.organize_life.model.FuturePlanCreate;
import com.backend.organize_life.model.FuturePlanUpdate;
import life_ecom_logic_core.eddie.repository.FuturePlanRepository;
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
        return futurePlanRepository.findById(id)
                .map(futurePlanMapper::toDto)
                .orElse(null);
    }

    @Override
    public FuturePlan create(FuturePlanCreate create) {
        log.info("Creating new future plan");
        return futurePlanMapper.toDto(
                futurePlanRepository.save(futurePlanMapper.toEntity(create)));
    }

    @Override
    public FuturePlan update(Integer id, FuturePlanUpdate update) {
        log.info("Updating future plan with id: {}", id);
        return futurePlanRepository.findById(id)
                .map(entity -> futurePlanMapper.toDto(
                        futurePlanRepository.save(futurePlanMapper.updateEntity(update, entity))))
                .orElse(null);
    }

    @Override
    public boolean delete(Integer id) {
        log.info("Deleting future plan with id: {}", id);
        if (!futurePlanRepository.existsById(id)) {
            return false;
        }
        futurePlanRepository.deleteById(id);
        return true;
    }
}
