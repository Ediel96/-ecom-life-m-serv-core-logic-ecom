package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.*;

import java.util.UUID;

public interface RecurringTransactionService {

    PageRecurringTransaction list(Integer page, Integer size, String sort,
                                  UUID userId, Boolean isActive, Boolean isLifestyle,
                                  FrequencyType frequency);

    RecurringTransaction get(Long id);

    RecurringTransaction create(RecurringTransactionCreate create);

    RecurringTransaction update(Long id, RecurringTransactionUpdate update);

    boolean delete(Long id);

    /** Flips is_active flag. Returns the updated entity or null if not found. */
    RecurringTransaction toggle(Long id);
}
