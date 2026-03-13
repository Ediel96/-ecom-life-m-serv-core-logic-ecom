package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.PlanMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanMovementRepository extends JpaRepository<PlanMovementEntity, Integer> {

    @Query("SELECT m FROM PlanMovementEntity m WHERE (:planId IS NULL OR m.plan.id = :planId)")
    List<PlanMovementEntity> findByFilters(Integer planId);
}
