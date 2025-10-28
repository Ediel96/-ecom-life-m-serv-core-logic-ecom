package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.Account;
import com.backend.organize_life.model.AccountCreate;
import com.backend.organize_life.model.AccountUpdate;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<Account> list(@Nullable UUID userId);
    Optional<Account> get(Integer id);
    Account create(AccountCreate accountCreate);
    Optional<Account> update(Integer id, AccountUpdate accountUpdate);
    boolean delete(Integer id);
}
