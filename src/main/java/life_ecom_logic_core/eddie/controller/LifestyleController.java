package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.LifestyleApiDelegate;
import com.backend.organize_life.model.LifestyleSummary;
import com.backend.organize_life.model.PageTransaction;
import life_ecom_logic_core.eddie.service.LifestyleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller delegate for Lifestyle analytics API operations.
 * Implements the LifestyleApiDelegate interface generated from OpenAPI spec.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LifestyleController implements LifestyleApiDelegate {

    private final LifestyleService lifestyleService;

    /** GET /lifestyle/summary */
    @Override
    public ResponseEntity<List<LifestyleSummary>> lifestyleSummaryGet(
            UUID userId,
            LocalDate monthFrom,
            LocalDate monthTo) {

        log.debug("Fetching lifestyle summary for userId: {}", userId);
        List<LifestyleSummary> result = lifestyleService.summary(userId, monthFrom, monthTo);
        return ResponseEntity.ok(result);
    }

    /** GET /lifestyle/transactions — userId is first because it is required in the spec. */
    @Override
    public ResponseEntity<PageTransaction> lifestyleTransactionsGet(
            UUID userId,
            Integer page,
            Integer size,
            String sort,
            Integer categoryId,
            OffsetDateTime dateFrom,
            OffsetDateTime dateTo) {

        log.debug("Listing lifestyle transactions for userId: {}", userId);
        PageTransaction result = lifestyleService.listTransactions(
                page, size, sort, userId, categoryId, dateFrom, dateTo);
        return ResponseEntity.ok(result);
    }
}
