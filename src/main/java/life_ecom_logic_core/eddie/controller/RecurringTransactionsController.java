package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.RecurringTransactionsApiDelegate;
import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.service.RecurringTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Controller delegate for Recurring Transaction API operations.
 * Implements the RecurringTransactionsApiDelegate interface generated from OpenAPI spec.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecurringTransactionsController implements RecurringTransactionsApiDelegate {

    private final RecurringTransactionService recurringService;

    /** GET /recurring-transactions */
    @Override
    public ResponseEntity<PageRecurringTransaction> recurringTransactionsGet(
            Integer page,
            Integer size,
            String sort,
            UUID userId,
            Boolean isActive,
            Boolean isLifestyle,
            FrequencyType frequency) {

        log.debug("Listing recurring transactions - userId: {}, isActive: {}", userId, isActive);
        PageRecurringTransaction result =
                recurringService.list(page, size, sort, userId, isActive, isLifestyle, frequency);
        return ResponseEntity.ok(result);
    }

    /** POST /recurring-transactions */
    @Override
    public ResponseEntity<RecurringTransaction> recurringTransactionsPost(
            RecurringTransactionCreate recurringTransactionCreate) {

        log.info("Creating recurring transaction");
        RecurringTransaction created = recurringService.create(recurringTransactionCreate);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** GET /recurring-transactions/{id} */
    @Override
    public ResponseEntity<RecurringTransaction> recurringTransactionsIdGet(Long id) {
        log.debug("Fetching recurring transaction id: {}", id);
        RecurringTransaction result = recurringService.get(id);
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /** PUT /recurring-transactions/{id} */
    @Override
    public ResponseEntity<RecurringTransaction> recurringTransactionsIdPut(
            Long id, RecurringTransactionUpdate recurringTransactionUpdate) {

        log.info("Updating recurring transaction id: {}", id);
        RecurringTransaction updated = recurringService.update(id, recurringTransactionUpdate);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    /** DELETE /recurring-transactions/{id} */
    @Override
    public ResponseEntity<Void> recurringTransactionsIdDelete(Long id) {
        log.info("Deleting recurring transaction id: {}", id);
        try {
            if (recurringService.delete(id)) return ResponseEntity.noContent().build();
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Error deleting recurring transaction id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** PATCH /recurring-transactions/{id}/toggle */
    @Override
    public ResponseEntity<RecurringTransaction> recurringTransactionsIdTogglePatch(Long id) {
        log.info("Toggling recurring transaction id: {}", id);
        RecurringTransaction toggled = recurringService.toggle(id);
        if (toggled == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toggled);
    }
}
