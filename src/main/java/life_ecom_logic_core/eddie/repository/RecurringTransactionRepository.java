package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.FrequencyType;
import life_ecom_logic_core.eddie.domain.RecurringTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransactionEntity, Long> {

    @Query("""
        select r from RecurringTransactionEntity r
        where r.user.id = coalesce(:userId, r.user.id)
          and (:isActive IS NULL OR r.active = :isActive)
          and (:isLifestyle IS NULL OR r.lifestyle = :isLifestyle)
          and (:frequency IS NULL OR r.frequency = :frequency)
        """)
    Page<RecurringTransactionEntity> search(
            @Param("userId") UUID userId,
            @Param("isActive") Boolean isActive,
            @Param("isLifestyle") Boolean isLifestyle,
            @Param("frequency") FrequencyType frequency,
            Pageable pageable
    );
}
