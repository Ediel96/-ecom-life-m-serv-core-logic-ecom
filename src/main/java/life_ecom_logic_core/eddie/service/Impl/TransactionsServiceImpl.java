package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.config.JwtUtil;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.repository.TransactionRepository;
import life_ecom_logic_core.eddie.service.TransactionsService;
import life_ecom_logic_core.eddie.service.mapper.TransactionEnumMapper;
import life_ecom_logic_core.eddie.service.mapper.TransactionsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionsMapper transactionsMapper;

    private final TransactionEnumMapper transactionEnumMapper = new TransactionEnumMapper();

    @Override
    public PageTransaction list(Integer page, Integer size, String sort, UUID userId, Integer accountId, Integer categoryId, TransactionType transactionType, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        log.info("page: {}, size: {}, sort: {}, userId: {}, accountId: {}, categoryId: {}, transactionType: {}, dateFrom: {}, dateTo: {}",
                page, size, sort, userId, accountId, categoryId, transactionType, dateFrom, dateTo);

        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 20 : size;

        Pageable pageable = PageRequest.of(p, s, buildSort(sort)); // apply sort

        Page<TransactionEntity> pageEntities = transactionRepository.search(
                userId,
                accountId,
                categoryId,
                transactionType != null ? transactionEnumMapper.mapToEntityEnum(transactionType) : null,
                dateFrom,
                dateTo,
                pageable
        );

        PageTransaction pages = new PageTransaction();
        pages.setContent(pageEntities.getContent().stream().map(transactionsMapper::toDto).collect(java.util.stream.Collectors.toList()));
        pages.setPage(p);
        pages.setSize(s);
        pages.setTotalElements(pageEntities.getTotalElements());
        pages.setTotalPages((int) pageEntities.getTotalPages());
        return pages;
    }

    @Override
    public Transaction get(Long id) {
        Optional<TransactionEntity> transactionEntity = transactionRepository.findById(id);
        return transactionEntity.map(transactionsMapper::toDto).orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        // TODO: delete by id
        Optional<TransactionEntity> existing = transactionRepository.findById(id);
        if (existing.isEmpty()) {
            return false;
        }
        transactionRepository.deleteById(id);
        return true;
    }

    @Override
    public Transaction update(Long id, TransactionUpdate update) {
        return transactionRepository.findById(id)
                .map(e -> {
                    TransactionEntity updateEntity = transactionsMapper.updateEntity(update, e);
                    return transactionsMapper.toDto(transactionRepository.save(updateEntity));
                })
                .orElse(null);
    }

    @Override
    public Transaction create(TransactionCreate create) {

        String token = jwtUtil.extractTokenFromRequest();
        UUID idUserToken = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        if ( create != null && create.getUserId() != null && role.equals("ROLE_USER") )
            create.userId( idUserToken);

        else if ( create != null && create.getUserId() == null ) create.userId(idUserToken);

        TransactionEntity transactionEntity =
                transactionRepository.save(transactionsMapper.toEntity(create));
        return transactionsMapper.toDto(transactionEntity);
    }

    // Keep blocking only what you really don't want to sort by
    private static final java.util.Set<String> BLOCKED_SORT_FIELDS =
            java.util.Set.of("updated_at","date","updatedAt"); // 'created_at' unblocked

    // Map API fields (snake_case) to entity property names (camelCase)
    private static final java.util.Map<String, String> SORT_ALIASES = java.util.Map.ofEntries(
            java.util.Map.entry("created_at", "createdAt"),
            java.util.Map.entry("updated_at", "updatedAt"),
            java.util.Map.entry("transaction_type", "transactionType"),
            java.util.Map.entry("amount", "amount"),
            java.util.Map.entry("description", "description"),
            java.util.Map.entry("date", "date")
    );

    private Sort buildSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.unsorted();
        }
        java.util.List<Sort.Order> orders = new java.util.ArrayList<>();
        for (String part : sortParam.split(";")) {
            String[] tokens = part.trim().split(",");
            if (tokens.length == 0) continue;

            String raw = tokens[0].trim();
            // basic whitelist to avoid injection, allow nested props like account.id
            if (!raw.matches("[A-Za-z0-9_\\.]+")) continue;

            // map snake_case to entity property
            String property = SORT_ALIASES.getOrDefault(raw, raw);

            // skip blocked fields after mapping
            if (BLOCKED_SORT_FIELDS.contains(raw) || BLOCKED_SORT_FIELDS.contains(property)) continue;

            Sort.Direction dir = (tokens.length > 1 && "desc".equalsIgnoreCase(tokens[1].trim()))
                    ? Sort.Direction.DESC : Sort.Direction.ASC;

            orders.add(new Sort.Order(dir, property));
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}