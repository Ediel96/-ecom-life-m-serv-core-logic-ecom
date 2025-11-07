package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.*;
import jakarta.persistence.criteria.Path;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.repository.TransactionRepository;
import life_ecom_logic_core.eddie.service.TransactionsService;
import life_ecom_logic_core.eddie.service.mapper.TransactionsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionsMapper transactionsMapper;

    @Override
    public PageTransaction list(Integer page, Integer size, String sort, UUID userId, Integer accountId, Integer categoryId, TransactionType transactionType, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        log.info("page: {}, size: {}, sort: {}, userId: {}, accountId: {}, categoryId: {}, transactionType: {}, dateFrom: {}, dateTo: {}",
                page, size, sort, userId, accountId, categoryId, transactionType, dateFrom, dateTo);

        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 20 : size;
        Pageable pageable = org.springframework.data.domain.PageRequest.of(p, s);

        // Build Specification dynamically and resiliently to different entity mappings
        Specification<TransactionEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                Path<?> userPath;
                try {
                    userPath = root.get("userId"); // scalar FK property
                } catch (IllegalArgumentException ex) {
                    // fallback: relation 'user' with 'id'
                    userPath = root.get("user").get("id");
                }
                predicates.add(cb.equal(userPath, userId));
            }

            if (accountId != null) {
                Path<?> accountPath;
                try {
                    accountPath = root.get("accountId");
                } catch (IllegalArgumentException ex) {
                    accountPath = root.get("account").get("id");
                }
                predicates.add(cb.equal(accountPath, accountId));
            }

            if (categoryId != null) {
                Path<?> categoryPath;
                try {
                    categoryPath = root.get("categoryId");
                } catch (IllegalArgumentException ex) {
                    categoryPath = root.get("category").get("id");
                }
                predicates.add(cb.equal(categoryPath, categoryId));
            }

            if (transactionType != null) {
                // compare using string label to avoid binding to DB enum type; normalize case
                try {
                    Path<?> typePath = root.get("transactionType");
                    predicates.add(cb.or(
                            cb.equal(typePath.as(String.class), transactionType.name()),
                            cb.equal(cb.lower(typePath.as(String.class)), transactionType.name().toLowerCase())
                    ));
                } catch (IllegalArgumentException ex1) {
                    try {
                        Path<?> typePath = root.get("type");
                        predicates.add(cb.or(
                                cb.equal(typePath.as(String.class), transactionType.name()),
                                cb.equal(cb.lower(typePath.as(String.class)), transactionType.name().toLowerCase())
                        ));
                    } catch (IllegalArgumentException ex2) {
                        // attribute not found; ignore filter
                    }
                }
            }

            if (dateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
            }
            if (dateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), dateTo));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<TransactionEntity> pageEntities = transactionRepository.findAll(spec, pageable);

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
        TransactionEntity transactionEntity =
                transactionRepository.save(transactionsMapper.toEntity(create));
        return transactionsMapper.toDto(transactionEntity);
    }
}