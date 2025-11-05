package life_ecom_logic_core.eddie.repository;

import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("""
        select t from TransactionEntity t
        where (:userId is null or t.user.id = :userId)
          and (:accountId is null or t.account.id = :accountId)
          and (:categoryId is null or t.category.id = :categoryId)
          and (:transactionType is null or t.transactionType = :transactionType)
          and (:dateFrom is null or t.date >= :dateFrom)
          and (:dateTo is null or t.date <= :dateTo)
        """)
    Page<TransactionEntity> search(
            @Param("userId") UUID userId,
            @Param("accountId") Integer accountId,
            @Param("categoryId") Integer categoryId,
            @Param("transactionType") TransactionType transactionType,
            @Param("dateFrom") OffsetDateTime dateFrom,
            @Param("dateTo") OffsetDateTime dateTo,
            Pageable pageable
    );

}
