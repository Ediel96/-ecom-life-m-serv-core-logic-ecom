package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.Account;
import com.backend.organize_life.model.AccountCreate;
import com.backend.organize_life.model.AccountUpdate;
import life_ecom_logic_core.eddie.config.JwtUtil;
import life_ecom_logic_core.eddie.domain.AccountEntity;
import life_ecom_logic_core.eddie.domain.UserEntity;
import life_ecom_logic_core.eddie.repository.AccountRepository;
import life_ecom_logic_core.eddie.repository.UserRepository;
import life_ecom_logic_core.eddie.service.AccountService;
import life_ecom_logic_core.eddie.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for Account operations.
 * Handles CRUD operations and business logic for user accounts.
 *
 * @author eddie
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String DEFAULT_ACCOUNT_TYPE = "DEFAULT";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    /**
     * Lists accounts based on user role and optional userId filter.
     * Admin users can view all accounts; regular users see only their own.
     *
     * @param userId optional user ID filter
     * @return list of accounts
     */
    @Override
    public List<Account> list(@Nullable UUID userId) {
        String token = jwtUtil.extractTokenFromRequest();
        UUID tokenUserId = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);

        List<AccountEntity> entities;

        if (ROLE_ADMIN.equals(role) && userId == null) {
            entities = tokenUserId != null
                    ? accountRepository.findByUserId(tokenUserId, DEFAULT_ACCOUNT_TYPE)
                    : accountRepository.findAll();
        } else {
            entities = accountRepository.findByUserId(tokenUserId, DEFAULT_ACCOUNT_TYPE);
        }

        return mapEntitiesToDto(entities);
    }

    /**
     * Retrieves a specific account by ID.
     *
     * @param id the account ID
     * @return optional containing the account if found
     */
    @Override
    public Optional<Account> get(Integer id) {
        log.debug("Fetching account with id: {}", id);
        return accountRepository.findById(id.longValue())
                .map(AccountMapper::toDto);
    }

    /**
     * Creates a new account for the authenticated user.
     *
     * @param accountCreate the account creation data
     * @return the created account
     */
    @Override
    public Account create(AccountCreate accountCreate) {
        log.info("Creating account with name: {} for userId: {}",
                accountCreate.getName(), accountCreate.getUserId());

        String token = jwtUtil.extractTokenFromRequest();
        UUID userId = jwtUtil.extractUserId(token);
        UserEntity user = userRepository.findById(userId);

        AccountEntity entity = AccountMapper.toEntity(accountCreate, user);
        AccountEntity savedEntity = accountRepository.save(entity);

        return AccountMapper.toDto(savedEntity);
    }

    /**
     * Updates an existing account.
     *
     * @param id the account ID to update
     * @param accountUpdate the update data
     * @return optional containing the updated account if found
     */
    @Override
    public Optional<Account> update(Integer id, AccountUpdate accountUpdate) {
        log.info("Updating account with id: {}", id);
        return accountRepository.findById(id.longValue())
                .map(entity -> {
                    AccountMapper.updateEntity(accountUpdate, entity);
                    AccountEntity savedEntity = accountRepository.save(entity);
                    return AccountMapper.toDto(savedEntity);
                });
    }

    /**
     * Deletes an account by ID.
     *
     * @param id the account ID to delete
     * @return true if deleted, false if not found
     */
    @Override
    public boolean delete(Integer id) {
        log.info("Deleting account with id: {}", id);
        if (!accountRepository.existsById(id.longValue())) {
            return false;
        }
        accountRepository.deleteById(id.longValue());
        return true;
    }

    /**
     * Maps a list of AccountEntity to Account DTOs.
     */
    private List<Account> mapEntitiesToDto(List<AccountEntity> entities) {
        return entities.stream()
                .map(AccountMapper::toDto)
                .collect(Collectors.toList());
    }
}