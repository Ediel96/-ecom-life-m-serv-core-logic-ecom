package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.*;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface TransactionsService {
    PageTransaction list(Integer page,
                         Integer size,
                         String sort,
                         UUID userId,
                         Integer accountId,
                         Integer categoryId,
                         TransactionType transactionType,
                         OffsetDateTime dateFrom,
                         OffsetDateTime dateTo);

    Transaction get(Long id);

    boolean delete(Long id);

    Transaction update(Long id, TransactionUpdate update);

    Transaction create(TransactionCreate create);
}