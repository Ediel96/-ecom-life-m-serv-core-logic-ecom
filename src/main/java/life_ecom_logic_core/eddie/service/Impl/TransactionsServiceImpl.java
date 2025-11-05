package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.*;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.repository.TransactionRepository;
import life_ecom_logic_core.eddie.service.TransactionsService;
import life_ecom_logic_core.eddie.service.mapper.TransactionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionsServiceImpl implements TransactionsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionsMapper transactionsMapper;

    @Override
    public PageTransaction list(Integer page, Integer size, String sort, UUID userId, Integer accountId, Integer categoryId, TransactionType transactionType, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        // implement paging and mapping using repository
        int p = (page == null || page < 0) ? 0 : page;
        int s = (size == null || size <= 0) ? 20 : size;
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(p, s);
        org.springframework.data.domain.Page<TransactionEntity> pageEntities = transactionRepository.search(userId,
                accountId,
                categoryId,
                transactionType,
                dateFrom,
                dateTo,
                pageable);

        PageTransaction pages = new PageTransaction();
        // map entities to DTOs
        pages.setContent(pageEntities.getContent().stream().map(transactionsMapper::toDto).collect(java.util.stream.Collectors.toList()));
        // populate pagination metadata
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