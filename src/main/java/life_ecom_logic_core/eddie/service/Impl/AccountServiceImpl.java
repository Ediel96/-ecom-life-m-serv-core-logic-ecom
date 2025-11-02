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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Account> list(@Nullable UUID userId) {
        String token = jwtUtil.extractTokenFromRequest();
        UUID idUserToken = jwtUtil.extractUserId(token);
        String role = jwtUtil.extractRole(token);
        List<AccountEntity> entities;

        if (role.equals("ROLE_ADMIN") && userId == null) {
            entities = idUserToken != null
                    ? accountRepository.findByUserId(idUserToken)
                    : accountRepository.findAll();
            return mapEntitiesToDto(entities);
        }

        entities = accountRepository.findByUserId(idUserToken);
        return mapEntitiesToDto(entities);
    }

    private List<Account> mapEntitiesToDto(List<AccountEntity> entities) {
        return entities.stream()
                .map(AccountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Account> get(Integer id) {
        return accountRepository.findById(id.longValue())
                .map(AccountMapper::toDto);
    }

    @Override
    public Account create(AccountCreate accountCreate) {
        log.info("creating account with name {} id {}", accountCreate.getName(), accountCreate.getUserId());
        String token =  jwtUtil.extractTokenFromRequest();
        UUID userId = jwtUtil.extractUserId(token);
        UserEntity user =  userRepository.findById(userId);
        AccountEntity entity = AccountMapper.toEntity(accountCreate, user);
        accountRepository.save(entity);
        return AccountMapper.toDto(entity);
    }

    @Override
    public Optional<Account> update(Integer id, AccountUpdate accountUpdate) {
        log.info("updating account with id {}", id);
        return accountRepository.findById(id.longValue()).map(entity -> {
            AccountMapper.updateEntity(accountUpdate, entity);
            return AccountMapper.toDto(accountRepository.save(entity));
        });
    }

    @Override
    public boolean delete(Integer id) {
        if (!accountRepository.existsById(id.longValue())) return false;
        accountRepository.deleteById(id.longValue());
        return true;
    }
}