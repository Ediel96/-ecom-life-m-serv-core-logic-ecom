package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.FuturePlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FuturePlanRepository extends JpaRepository<FuturePlanEntity, Integer> {

    @Query("SELECT f FROM FuturePlanEntity f WHERE (:userId IS NULL OR f.user.id = :userId) AND (:status IS NULL OR f.status = :status)")
    List<FuturePlanEntity> findByFilters(UUID userId, String status);
}
