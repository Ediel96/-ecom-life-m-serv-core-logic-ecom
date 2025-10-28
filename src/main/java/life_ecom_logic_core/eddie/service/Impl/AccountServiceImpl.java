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
        List<AccountEntity> entities = userId != null
                ? accountRepository.findByUserId(userId)
                : accountRepository.findAll();

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
        log.info("creating account");
        String token =  jwtUtil.extractTokenFromRequest();
        log.info("token: " + token);
        UUID userId = jwtUtil.extractUserId(token);
        log.info("user id: " + userId);
        UserEntity user =  userRepository.findById(userId);
        log.info("user info: " +  user);
        AccountEntity entity = AccountMapper.toEntity(accountCreate, user);
        accountRepository.save(entity);
        return AccountMapper.toDto(entity);
    }

    @Override
    public Optional<Account> update(Integer id, AccountUpdate accountUpdate) {
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