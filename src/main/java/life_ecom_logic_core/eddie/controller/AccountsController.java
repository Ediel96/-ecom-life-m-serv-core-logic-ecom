package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.AccountsApiDelegate;
import com.backend.organize_life.model.Account;
import com.backend.organize_life.model.AccountCreate;
import com.backend.organize_life.model.AccountUpdate;
import life_ecom_logic_core.eddie.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AccountsController  implements AccountsApiDelegate {

    @Autowired
    private AccountService accountService;

    @Override
    public ResponseEntity<List<Account>> accountsGet(UUID userId) {
        List<Account> accounts = accountService.list(userId);
        return ResponseEntity.ok(accounts);
    }

    @Override
    public ResponseEntity<Account> accountsIdGet(Integer id) {
        return accountService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Account> accountsPost(AccountCreate accountCreate) {
        Account created = accountService.create(accountCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Account> accountsIdPut(Integer id, AccountUpdate accountUpdate) {
        return accountService.update(id, accountUpdate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> accountsIdDelete(Integer id) {
        boolean deleted = accountService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
