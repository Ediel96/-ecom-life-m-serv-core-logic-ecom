package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.TransactionsApiDelegate;
import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.service.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
public class TransactionsController implements TransactionsApiDelegate {

    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @Override
    public ResponseEntity<PageTransaction> transactionsGet(Integer page,
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
        // Coalesce: prefer camelCase params (userId, accountId, ...) and fall back to snake_case variants (userId2, accountId2, ...)
        UUID finalUserId = userId != null ? userId : userId2;
        Integer finalAccountId = accountId != null ? accountId : accountId2;
        Integer finalCategoryId = categoryId != null ? categoryId : categoryId2;
        TransactionType finalTransactionType = transactionType != null ? transactionType : transactionType2;
        OffsetDateTime finalDateFrom = dateFrom != null ? dateFrom : dateFrom2;
        OffsetDateTime finalDateTo = dateTo != null ? dateTo : dateTo2;

        // Use coalesced parameters in the service call
        PageTransaction result = transactionsService.list(page, size, sort, finalUserId, finalAccountId, finalCategoryId, finalTransactionType, finalDateFrom, finalDateTo);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> transactionsIdDelete(Long id) {
        try {
            if (transactionsService.delete(id)) return  ResponseEntity.ok().build();
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        } catch (Exception ex) {
            log.error("Error deleting transaction with id {}", id, ex);
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Override
    public ResponseEntity<Transaction> transactionsIdGet(Long id) {
        Transaction t = transactionsService.get(id);
        if (t == null) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
        return ResponseEntity.ok(t);
    }

    @Override
    public ResponseEntity<Transaction> transactionsIdPut(Long id, TransactionUpdate transactionUpdate) {
        Transaction updated = transactionsService.update(id, transactionUpdate);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Transaction> transactionsPost(TransactionCreate transactionCreate) {
        Transaction created = transactionsService.create(transactionCreate);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
