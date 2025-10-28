package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findById(UUID id);

}
