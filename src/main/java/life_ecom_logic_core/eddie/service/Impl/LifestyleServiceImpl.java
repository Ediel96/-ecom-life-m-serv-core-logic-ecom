package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.LifestyleSummary;
import com.backend.organize_life.model.PageTransaction;
import com.backend.organize_life.model.Transaction;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.repository.TransactionRepository;
import life_ecom_logic_core.eddie.repository.projection.LifestyleSummaryRow;
import life_ecom_logic_core.eddie.service.LifestyleService;
import life_ecom_logic_core.eddie.service.mapper.TransactionsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LifestyleServiceImpl implements LifestyleService {

    private static final int DEFAULT_PAGE      = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final TransactionRepository transactionRepository;
    private final TransactionsMapper    transactionsMapper;

    @Override
    public List<LifestyleSummary> summary(UUID userId, LocalDate monthFrom, LocalDate monthTo) {
        log.debug("Fetching lifestyle summary for userId: {}", userId);

        String monthFromStr = monthFrom != null
                ? monthFrom.atStartOfDay().atOffset(ZoneOffset.UTC).toString()
                : null;
        String monthToStr = monthTo != null
                ? monthTo.atStartOfDay().atOffset(ZoneOffset.UTC).toString()
                : null;

        List<LifestyleSummaryRow> rows = transactionRepository.findLifestyleSummary(
                userId.toString(), monthFromStr, monthToStr);

        return rows.stream().map(this::mapSummaryRow).collect(Collectors.toList());
    }

    @Override
    public PageTransaction listTransactions(Integer page, Integer size, String sort,
                                             UUID userId, Integer categoryId,
                                             OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        log.debug("Listing lifestyle transactions for userId: {}", userId);

        int pageNum  = normalize(page);
        int pageSize = normalizeSize(size);
        Pageable pageable = PageRequest.of(pageNum, pageSize, buildSort(sort));

        Page<TransactionEntity> result = transactionRepository.findLifestyleTransactions(
                userId, categoryId, dateFrom, dateTo, pageable);

        return buildPage(result, pageNum, pageSize);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private LifestyleSummary mapSummaryRow(LifestyleSummaryRow row) {
        LifestyleSummary dto = new LifestyleSummary();
        dto.setUserId(row.getUserId());
        dto.setCategoryName(row.getCategoryName());
        dto.setMonth(row.getMonth());
        dto.setTransactionCount(row.getTransactionCount() != null ? row.getTransactionCount() : 0L);
        dto.setTotalAmount(row.getTotalAmount());
        dto.setAverageAmount(row.getAverageAmount());
        return dto;
    }

    private int normalize(Integer page) {
        return (page == null || page < 0) ? DEFAULT_PAGE : page;
    }

    private int normalizeSize(Integer size) {
        return (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }

    private Sort buildSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) return Sort.by(Sort.Direction.DESC, "date");
        String[] parts = sortParam.split(",");
        String field  = parts[0].trim();
        Sort.Direction dir = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    private PageTransaction buildPage(Page<TransactionEntity> page, int pageNum, int pageSize) {
        List<Transaction> content = page.getContent().stream()
                .map(transactionsMapper::toDto)
                .collect(Collectors.toList());
        PageTransaction response = new PageTransaction();
        response.setContent(content);
        response.setPage(pageNum);
        response.setSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
