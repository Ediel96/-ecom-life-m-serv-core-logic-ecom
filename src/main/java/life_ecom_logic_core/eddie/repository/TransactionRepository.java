package life_ecom_logic_core.eddie.repository;

import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.TransactionEntity;
import life_ecom_logic_core.eddie.domain.TransactionTypeEnum;
import life_ecom_logic_core.eddie.repository.projection.LifestyleSummaryRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
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

    /** Paginated lifestyle-tagged expense transactions for a user with optional filters. */
    @Query("""
        select t from TransactionEntity t
        where t.lifestyle = true
          and t.user.id = :userId
          and (:categoryId IS NULL OR t.category.id = :categoryId)
          and (:dateFrom IS NULL OR t.date >= :dateFrom)
          and (:dateTo IS NULL OR t.date <= :dateTo)
        """)
    Page<TransactionEntity> findLifestyleTransactions(
            @Param("userId") UUID userId,
            @Param("categoryId") Integer categoryId,
            @Param("dateFrom") OffsetDateTime dateFrom,
            @Param("dateTo") OffsetDateTime dateTo,
            Pageable pageable
    );

    /** Aggregated lifestyle summary from the lifestyle_summary view with optional date-range filters. */
    @Query(value = """
        SELECT
            user_id        AS userId,
            category_name  AS categoryName,
            month          AS month,
            transaction_count AS transactionCount,
            total_amount   AS totalAmount,
            average_amount AS averageAmount
        FROM lifestyle_summary
        WHERE user_id = :userId::uuid
          AND (:monthFrom IS NULL OR month >= :monthFrom::timestamptz)
          AND (:monthTo   IS NULL OR month <= :monthTo::timestamptz)
        ORDER BY month DESC
        """, nativeQuery = true)
    List<LifestyleSummaryRow> findLifestyleSummary(
            @Param("userId") String userId,
            @Param("monthFrom") String monthFrom,
            @Param("monthTo") String monthTo
    );

}

