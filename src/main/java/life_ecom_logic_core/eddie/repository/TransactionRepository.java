package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.TransactionEntity;
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

    @Query(value = """
        SELECT t.* FROM transactions t
        LEFT JOIN accounts a ON a.id = t.account_id
        LEFT JOIN categories c ON c.id = t.category_id
        WHERE (CAST(:userId AS text) IS NULL OR t.user_id = CAST(:userId AS uuid))
          AND (:accountId IS NULL OR t.account_id = :accountId)
          AND (:categoryId IS NULL OR t.category_id = :categoryId)
          AND (CAST(:transactionType AS text) IS NULL OR t.transaction_type = CAST(:transactionType AS transaction_type))
          AND (CAST(:dateFrom AS text) IS NULL OR t.date >= CAST(:dateFrom AS timestamptz))
          AND (CAST(:dateTo AS text) IS NULL OR t.date <= CAST(:dateTo AS timestamptz))
        """,
        countQuery = """
        SELECT COUNT(*) FROM transactions t
        LEFT JOIN accounts a ON a.id = t.account_id
        LEFT JOIN categories c ON c.id = t.category_id
        WHERE (CAST(:userId AS text) IS NULL OR t.user_id = CAST(:userId AS uuid))
          AND (:accountId IS NULL OR t.account_id = :accountId)
          AND (:categoryId IS NULL OR t.category_id = :categoryId)
          AND (CAST(:transactionType AS text) IS NULL OR t.transaction_type = CAST(:transactionType AS transaction_type))
          AND (CAST(:dateFrom AS text) IS NULL OR t.date >= CAST(:dateFrom AS timestamptz))
          AND (CAST(:dateTo AS text) IS NULL OR t.date <= CAST(:dateTo AS timestamptz))
        """,
        nativeQuery = true)
    Page<TransactionEntity> search(
            @Param("userId") String userId,
            @Param("accountId") Integer accountId,
            @Param("categoryId") Integer categoryId,
            @Param("transactionType") String transactionType,
            @Param("dateFrom") String dateFrom,
            @Param("dateTo") String dateTo,
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

