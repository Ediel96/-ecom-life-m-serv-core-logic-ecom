package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository  extends JpaRepository<AccountEntity, Long> {

    AccountEntity findAccountById(Long id);
    List<AccountEntity> findAll();

    @Query("SELECT a FROM AccountEntity a WHERE a.user.id = ?1 or a.type = ?2")
    List<AccountEntity> findByUserId(UUID id, String type);

}
