package life_ecom_logic_core.eddie.service.mapper;

import com.backend.organize_life.model.Account;
import com.backend.organize_life.model.AccountCreate;
import com.backend.organize_life.model.AccountUpdate;
import life_ecom_logic_core.eddie.domain.AccountEntity;
import life_ecom_logic_core.eddie.domain.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class AccountMapper {

    public static Account toDto(AccountEntity entity) {
        Account dto = new Account();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setName(entity.getName());
        dto.setBalance(entity.getBalance() != null ? entity.getBalance().doubleValue() : null);
        dto.setCurrency(entity.getCurrency());
        dto.setAccountType(entity.getAccountType());
        dto.setType(entity.getType());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static AccountEntity toEntity(AccountCreate create, UserEntity user) {
        if (create == null) {
            return null;
        }

        AccountEntity entity = new AccountEntity();

        if (user != null) {
            entity.setUser(user);
        } else if (create.getUserId() != null) {
            UserEntity u = new UserEntity();
            u.setId(create.getUserId());
            entity.setUser(u);
        }

        entity.setName(create.getName());
        entity.setBalance(create.getBalance() != null ? BigDecimal.valueOf(create.getBalance()) : null);
        entity.setType(create.getType());
        OffsetDateTime now = OffsetDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    public static AccountEntity updateEntity(AccountUpdate update, AccountEntity entity) {
        if (update == null || entity == null) {
            return entity;
        }
        if (update.getName() != null) {
            entity.setName(update.getName());
        }
        if (update.getBalance() != null) {
            entity.setBalance(BigDecimal.valueOf(update.getBalance()));
        }
        if (update.getType() != null) {
            entity.setType(update.getType());
        }
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

}
