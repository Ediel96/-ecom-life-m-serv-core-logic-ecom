package life_ecom_logic_core.eddie.repository;

import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.domain.TransactionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, JpaSpecificationExecutor<TransactionEntity> {

    @Query("""
        select t from TransactionEntity t
        where t.user.id = coalesce(:userId, t.user.id)
          and t.account.id = coalesce(:accountId, t.account.id)
          and t.category.id = coalesce(:categoryId, t.category.id)
          and t.transactionType = coalesce(:transactionType, t.transactionType)
          and t.date >= coalesce(:dateFrom, t.date)
          and t.date <= coalesce(:dateTo, t.date)
        """)
    Page<TransactionEntity> search(
            @Param("userId") UUID userId,
            @Param("accountId") Integer accountId,
            @Param("categoryId") Integer categoryId,
            @Param("transactionType") TransactionTypeEnum transactionType,
            @Param("dateFrom") OffsetDateTime dateFrom,
            @Param("dateTo") OffsetDateTime dateTo,
            Pageable pageable
    );

}
