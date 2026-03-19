package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.config.JwtUtil;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.repository.TransactionRepository;
import life_ecom_logic_core.eddie.service.TransactionsService;
import life_ecom_logic_core.eddie.service.mapper.TransactionEnumMapper;
import life_ecom_logic_core.eddie.service.mapper.TransactionsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for Transaction operations.
 * Handles CRUD operations and business logic for financial transactions.
 *
 * @author eddie
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private static final String ROLE_USER = "ROLE_USER";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    /** Fields blocked from sorting */
    private static final Set<String> BLOCKED_SORT_FIELDS = Set.of(
            "updated_at", "date", "updatedAt"
    );

    /** Mapping from API field names (snake_case) to entity property names (camelCase) */
    private static final Map<String, String> SORT_FIELD_ALIASES = Map.ofEntries(
            Map.entry("created_at", "createdAt"),
            Map.entry("updated_at", "updatedAt"),
            Map.entry("transaction_type", "transactionType"),
            Map.entry("amount", "amount"),
            Map.entry("description", "description"),
            Map.entry("date", "date")
    );

    private final JwtUtil jwtUtil;
    private final TransactionRepository transactionRepository;
    private final TransactionsMapper transactionsMapper;
    private final TransactionEnumMapper transactionEnumMapper = new TransactionEnumMapper();

    /**
     * Lists transactions with pagination and optional filters.
     *
     * @param page page number (0-based)
     * @param size page size
     * @param sort sorting criteria
     * @param userId user ID filter
     * @param accountId account ID filter
     * @param categoryId category ID filter
     * @param transactionType transaction type filter
     * @param dateFrom start date filter
     * @param dateTo end date filter
     * @return paginated list of transactions
     */
    @Override
    public PageTransaction list(Integer page, Integer size, String sort, UUID userId,
                                 Integer accountId, Integer categoryId,
                                 TransactionType transactionType, OffsetDateTime dateFrom,
                                 OffsetDateTime dateTo) {
        log.debug("Listing transactions - page: {}, size: {}, userId: {}, accountId: {}, categoryId: {}, type: {}", page, size, userId, accountId, categoryId, transactionType);

        int pageNumber = normalizePageNumber(page);
        int pageSize = normalizePageSize(size);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, buildSort(sort));

        Page<TransactionEntity> pageEntities = transactionRepository.search(
                userId,
                accountId,
                categoryId,
                transactionType != null ? transactionEnumMapper.mapToEntityEnum(transactionType) : null,
                dateFrom,
                dateTo,
                pageable
        );

        log.debug("Transactions query returned {} of {} total records", pageEntities.getNumberOfElements(), pageEntities.getTotalElements());
        return buildPageResponse(pageEntities, pageNumber, pageSize);
    }

    /**
     * Retrieves a specific transaction by ID.
     *
     * @param id the transaction ID
     * @return the transaction if found, null otherwise
     */
    @Override
    public Transaction get(Long id) {
        log.debug("Fetching transaction with id: {}", id);
        Transaction result = transactionRepository.findById(id)
                .map(transactionsMapper::toDto)
                .orElse(null);
        if (result == null) log.warn("Transaction not found with id: {}", id);
        return result;
    }

    /**
     * Deletes a transaction by ID.
     *
     * @param id the transaction ID to delete
     * @return true if deleted, false if not found
     */
    @Override
    public boolean delete(Long id) {
        log.info("Deleting transaction with id: {}", id);
        if (!transactionRepository.existsById(id)) {
            log.warn("Transaction not found for delete, id: {}", id);
            return false;
        }
        transactionRepository.deleteById(id);
        log.info("Transaction deleted id: {}", id);
        return true;
    }

    /**
     * Updates an existing transaction.
     *
     * @param id the transaction ID to update
     * @param update the update data
     * @return the updated transaction if found, null otherwise
     */
    @Override
    public Transaction update(Long id, TransactionUpdate update) {
        log.info("Updating transaction with id: {}", id);
        Transaction result = transactionRepository.findById(id)
                .map(entity -> {
                    TransactionEntity updatedEntity = transactionsMapper.updateEntity(update, entity);
                    Transaction saved = transactionsMapper.toDto(transactionRepository.save(updatedEntity));
                    log.info("Transaction updated id: {}", id);
                    return saved;
                })
                .orElse(null);
        if (result == null) log.warn("Transaction not found for update, id: {}", id);
        return result;
    }

    /**
     * Creates a new transaction.
     * The user ID is automatically set based on the authenticated user's role.
     *
     * @param create the transaction creation data
     * @return the created transaction
     */
    @Override
    public Transaction create(TransactionCreate create) {
        String token = jwtUtil.extractTokenFromRequest();
        UUID tokenUserId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        // Set user ID based on role
        if (create != null) {
            if (ROLE_USER.equals(role) || create.getUserId() == null) {
                create.userId(tokenUserId);
            }
        }

        log.info("Creating transaction for userId: {}, type: {}, amount: {}", tokenUserId, create != null ? create.getTransactionType() : null, create != null ? create.getAmount() : null);
        TransactionEntity savedEntity = transactionRepository.save(
                transactionsMapper.toEntity(create));
        Transaction result = transactionsMapper.toDto(savedEntity);
        log.info("Transaction created with id: {}", result.getId());
        return result;
    }

    // ================== Helper Methods ==================

    private int normalizePageNumber(Integer page) {
        return (page == null || page < 0) ? DEFAULT_PAGE : page;
    }

    private int normalizePageSize(Integer size) {
        return (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }

    private PageTransaction buildPageResponse(Page<TransactionEntity> pageEntities,
                                               int page, int size) {
        PageTransaction response = new PageTransaction();
        response.setContent(pageEntities.getContent().stream()
                .map(transactionsMapper::toDto)
                .collect(Collectors.toList()));
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(pageEntities.getTotalElements());
        response.setTotalPages(pageEntities.getTotalPages());
        return response;
    }

    private Sort buildSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String part : sortParam.split(";")) {
            String[] tokens = part.trim().split(",");
            if (tokens.length == 0) continue;

            String rawField = tokens[0].trim();

            // Validate field name format
            if (!rawField.matches("[A-Za-z0-9_\\.]+")) continue;

            // Map to entity property name
            String property = SORT_FIELD_ALIASES.getOrDefault(rawField, rawField);

            // Skip blocked fields
            if (isBlockedSortField(rawField, property)) continue;

            Sort.Direction direction = parseSortDirection(tokens);
            orders.add(new Sort.Order(direction, property));
        }

        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }

    private boolean isBlockedSortField(String rawField, String property) {
        return BLOCKED_SORT_FIELDS.contains(rawField) || BLOCKED_SORT_FIELDS.contains(property);
    }

    private Sort.Direction parseSortDirection(String[] tokens) {
        if (tokens.length > 1 && "desc".equalsIgnoreCase(tokens[1].trim())) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}