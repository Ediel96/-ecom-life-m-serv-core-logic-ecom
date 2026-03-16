package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.domain.RecurringTransactionEntity;
import life_ecom_logic_core.eddie.repository.RecurringTransactionRepository;
import life_ecom_logic_core.eddie.service.RecurringTransactionService;
import life_ecom_logic_core.eddie.service.mapper.RecurringTransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionServiceImpl implements RecurringTransactionService {

    private static final int DEFAULT_PAGE      = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final RecurringTransactionRepository recurringRepository;
    private final RecurringTransactionMapper     mapper;

    @Override
    public PageRecurringTransaction list(Integer page, Integer size, String sort,
                                         UUID userId, Boolean isActive, Boolean isLifestyle,
                                         FrequencyType frequency, Integer accountId,
                                         TransactionType transactionType,
                                         OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        log.debug("Listing recurring transactions - userId: {}, isActive: {}, accountId: {}", userId, isActive, accountId);

        int pageNum  = normalize(page, DEFAULT_PAGE);
        int pageSize = normalize(size, DEFAULT_PAGE_SIZE);
        // Native query uses fixed ORDER BY; ignore dynamic sort to avoid SQL injection
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        String userIdStr  = userId != null ? userId.toString() : null;
        String freqStr    = frequency != null ? frequency.name().toLowerCase(java.util.Locale.ROOT) : null;
        String transStr   = transactionType != null ? transactionType.name().toLowerCase(java.util.Locale.ROOT) : null;
        LocalDate from    = dateFrom != null ? dateFrom.toLocalDate() : null;
        LocalDate to      = dateTo   != null ? dateTo.toLocalDate()   : null;

        Page<RecurringTransactionEntity> result = recurringRepository.search(
                userIdStr, accountId, isActive, isLifestyle, transStr, freqStr, from, to, pageable);

        return buildPage(result, pageNum, pageSize);
    }

    @Override
    public RecurringTransaction get(Long id) {
        log.debug("Fetching recurring transaction id: {}", id);
        return recurringRepository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public RecurringTransaction create(RecurringTransactionCreate create) {
        log.info("Creating recurring transaction for userId: {}", create.getUserId());
        RecurringTransactionEntity saved = recurringRepository.save(mapper.toEntity(create));
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RecurringTransaction update(Long id, RecurringTransactionUpdate update) {
        log.info("Updating recurring transaction id: {}", id);
        return recurringRepository.findById(id)
                .map(entity -> mapper.toDto(recurringRepository.save(mapper.updateEntity(update, entity))))
                .orElse(null);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting recurring transaction id: {}", id);
        if (!recurringRepository.existsById(id)) return false;
        recurringRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public RecurringTransaction toggle(Long id) {
        log.info("Toggling is_active for recurring transaction id: {}", id);
        return recurringRepository.findById(id)
                .map(entity -> {
                    entity.setActive(!entity.isActive());
                    entity.setUpdatedAt(java.time.OffsetDateTime.now());
                    return mapper.toDto(recurringRepository.save(entity));
                })
                .orElse(null);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private int normalize(Integer value, int defaultValue) {
        return (value == null || value < 0) ? defaultValue : value;
    }

    private Sort buildSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) return Sort.by(Sort.Direction.ASC, "nextDueDate");
        String[] parts = sortParam.split(",");
        String field  = parts[0].trim();
        Sort.Direction dir = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    private PageRecurringTransaction buildPage(Page<RecurringTransactionEntity> page, int pageNum, int pageSize) {
        List<RecurringTransaction> content = page.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        PageRecurringTransaction response = new PageRecurringTransaction();
        response.setContent(content);
        response.setPage(pageNum);
        response.setSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
