package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.FuturePlanEntity;
import life_ecom_logic_core.eddie.repository.projection.PlanProgressRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuturePlanRepository extends JpaRepository<FuturePlanEntity, Integer> {

    @Query("SELECT f FROM FuturePlanEntity f WHERE (:userId IS NULL OR f.user.id = :userId) AND (:status IS NULL OR f.status = :status)")
    List<FuturePlanEntity> findByFilters(UUID userId, String status);

    @Query(value = """
            SELECT
                id,
                user_id               AS userId,
                title,
                icon,
                target_amount         AS targetAmount,
                target_date           AS targetDate,
                status,
                reminder_type         AS reminderType,
                reminder_day          AS reminderDay,
                saved,
                progress_pct          AS progressPct,
                avg_monthly_savings   AS avgMonthlySavings,
                estimated_completion_date AS estimatedCompletionDate
            FROM plan_progress
            WHERE (:userId IS NULL OR user_id = CAST(:userId AS uuid))
              AND (:status  IS NULL OR status  = :status)
            ORDER BY progress_pct DESC
            """, nativeQuery = true)
    List<PlanProgressRow> findProgress(@Param("userId") String userId,
                                       @Param("status")  String status);
}
