package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.TransactionsApiDelegate;
import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Controller delegate for Transaction-related API operations.
 * Implements the TransactionsApiDelegate interface generated from OpenAPI spec.
 *
 * @author eddie
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsController implements TransactionsApiDelegate {

    private final TransactionsService transactionsService;

    /**
     * Retrieves a paginated list of transactions with optional filters.
     *
     * @param page pagination page number
     * @param size page size
     * @param sort sorting criteria
     * @param userId user ID filter (camelCase)
     * @param accountId account ID filter (camelCase)
     * @param categoryId category ID filter (camelCase)
     * @param transactionType transaction type filter (camelCase)
     * @param dateFrom start date filter (camelCase)
     * @param dateTo end date filter (camelCase)
     * @param userId2 user ID filter (snake_case fallback)
     * @param accountId2 account ID filter (snake_case fallback)
     * @param categoryId2 category ID filter (snake_case fallback)
     * @param transactionType2 transaction type filter (snake_case fallback)
     * @param dateFrom2 start date filter (snake_case fallback)
     * @param dateTo2 end date filter (snake_case fallback)
     * @return paginated list of transactions
     */
    @Override
    public ResponseEntity<PageTransaction> transactionsGet(
            Integer page,
            Integer size,
            String sort,
            UUID userId,
            Integer accountId,
            Integer categoryId,
            TransactionType transactionType,
            OffsetDateTime dateFrom,
            OffsetDateTime dateTo,
            UUID userId2,
            Integer accountId2,
            Integer categoryId2,
            TransactionType transactionType2,
            OffsetDateTime dateFrom2,
            OffsetDateTime dateTo2) {

        // Coalesce: prefer camelCase params and fall back to snake_case variants
        UUID finalUserId = coalesce(userId, userId2);
        Integer finalAccountId = coalesce(accountId, accountId2);
        Integer finalCategoryId = coalesce(categoryId, categoryId2);
        TransactionType finalTransactionType = coalesce(transactionType, transactionType2);
        OffsetDateTime finalDateFrom = coalesce(dateFrom, dateFrom2);
        OffsetDateTime finalDateTo = coalesce(dateTo, dateTo2);

        log.debug("Fetching transactions - page: {}, size: {}, userId: {}", page, size, finalUserId);

        PageTransaction result = transactionsService.list(
                page, size, sort, finalUserId, finalAccountId,
                finalCategoryId, finalTransactionType, finalDateFrom, finalDateTo);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Deletes a transaction by ID.
     *
     * @param id the transaction ID to delete
     * @return 200 if deleted successfully
     */
    @Override
    public ResponseEntity<Void> transactionsIdDelete(Long id) {
        log.info("Deleting transaction with id: {}", id);
        try {
            if (transactionsService.delete(id)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Error deleting transaction with id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific transaction by ID.
     *
     * @param id the transaction ID
     * @return the transaction if found
     */
    @Override
    public ResponseEntity<Transaction> transactionsIdGet(Long id) {
        log.debug("Fetching transaction with id: {}", id);
        Transaction transaction = transactionsService.get(id);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }

    /**
     * Updates an existing transaction.
     *
     * @param id the transaction ID to update
     * @param transactionUpdate the update data
     * @return the updated transaction
     */
    @Override
    public ResponseEntity<Transaction> transactionsIdPut(Long id, TransactionUpdate transactionUpdate) {
        log.info("Updating transaction with id: {}", id);
        Transaction updated = transactionsService.update(id, transactionUpdate);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionCreate the transaction creation data
     * @return the created transaction with 201 status
     */
    @Override
    public ResponseEntity<Transaction> transactionsPost(TransactionCreate transactionCreate) {
        log.info("Creating new transaction");
        Transaction created = transactionsService.create(transactionCreate);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Helper method to coalesce two values, preferring the first non-null.
     */
    private <T> T coalesce(T primary, T fallback) {
        return primary != null ? primary : fallback;
    }
}
