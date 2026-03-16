package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.RecurringTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransactionEntity, Long> {

    @Query(
        value = """
            SELECT * FROM recurring_transactions r
            WHERE (:userId       IS NULL OR r.user_id          = CAST(:userId AS uuid))
              AND (:accountId    IS NULL OR r.account_id       = :accountId)
              AND (:isActive     IS NULL OR r.is_active        = :isActive)
              AND (:isLifestyle  IS NULL OR r.is_lifestyle     = :isLifestyle)
              AND (:transType    IS NULL OR r.transaction_type = CAST(:transType AS transaction_type))
              AND (:frequency    IS NULL OR r.frequency        = CAST(:frequency AS frequency_type))
              AND (:dateFrom     IS NULL OR r.next_due_date   >= :dateFrom)
              AND (:dateTo       IS NULL OR r.next_due_date   <= :dateTo)
            ORDER BY r.next_due_date ASC
            """,
        countQuery = """
            SELECT COUNT(*) FROM recurring_transactions r
            WHERE (:userId       IS NULL OR r.user_id          = CAST(:userId AS uuid))
              AND (:accountId    IS NULL OR r.account_id       = :accountId)
              AND (:isActive     IS NULL OR r.is_active        = :isActive)
              AND (:isLifestyle  IS NULL OR r.is_lifestyle     = :isLifestyle)
              AND (:transType    IS NULL OR r.transaction_type = CAST(:transType AS transaction_type))
              AND (:frequency    IS NULL OR r.frequency        = CAST(:frequency AS frequency_type))
              AND (:dateFrom     IS NULL OR r.next_due_date   >= :dateFrom)
              AND (:dateTo       IS NULL OR r.next_due_date   <= :dateTo)
            """,
        nativeQuery = true
    )
    Page<RecurringTransactionEntity> search(
            @Param("userId")      String userId,
            @Param("accountId")   Integer accountId,
            @Param("isActive")    Boolean isActive,
            @Param("isLifestyle") Boolean isLifestyle,
            @Param("transType")   String transType,
            @Param("frequency")   String frequency,
            @Param("dateFrom")    LocalDate dateFrom,
            @Param("dateTo")      LocalDate dateTo,
            Pageable pageable
    );
}
