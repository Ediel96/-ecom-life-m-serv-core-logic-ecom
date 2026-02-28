package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.AccountsApiDelegate;
import com.backend.organize_life.model.Account;
import com.backend.organize_life.model.AccountCreate;
import com.backend.organize_life.model.AccountUpdate;
import life_ecom_logic_core.eddie.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Controller delegate for Account-related API operations.
 * Implements the AccountsApiDelegate interface generated from OpenAPI spec.
 *
 * @author eddie
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountsController implements AccountsApiDelegate {

    private final AccountService accountService;

    /**
     * Retrieves all accounts for a specific user.
     *
     * @param userId the user ID to filter accounts (optional)
     * @return list of accounts
     */
    @Override
    public ResponseEntity<List<Account>> accountsGet(UUID userId) {
        log.debug("Fetching accounts for userId: {}", userId);
        List<Account> accounts = accountService.list(userId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Retrieves a specific account by ID.
     *
     * @param id the account ID
     * @return the account if found, 404 otherwise
     */
    @Override
    public ResponseEntity<Account> accountsIdGet(Integer id) {
        log.debug("Fetching account with id: {}", id);
        return accountService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new account.
     *
     * @param accountCreate the account creation data
     * @return the created account with 201 status
     */
    @Override
    public ResponseEntity<Account> accountsPost(AccountCreate accountCreate) {
        log.info("Creating account with name: {}", accountCreate.getName());
        Account created = accountService.create(accountCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing account.
     *
     * @param id the account ID to update
     * @param accountUpdate the update data
     * @return the updated account if found, 404 otherwise
     */
    @Override
    public ResponseEntity<Account> accountsIdPut(Integer id, AccountUpdate accountUpdate) {
        log.info("Updating account with id: {}", id);
        return accountService.update(id, accountUpdate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an account by ID.
     *
     * @param id the account ID to delete
     * @return 204 if deleted, 404 if not found
     */
    @Override
    public ResponseEntity<Void> accountsIdDelete(Integer id) {
        log.info("Deleting account with id: {}", id);
        boolean deleted = accountService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
