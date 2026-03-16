package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.PlanTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanTaskRepository extends JpaRepository<PlanTaskEntity, Integer> {

    List<PlanTaskEntity> findByPlanId(Integer planId);
}
