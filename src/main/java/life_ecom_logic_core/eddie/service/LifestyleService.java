package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.LifestyleSummary;
import com.backend.organize_life.model.PageTransaction;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface LifestyleService {

    /**
     * Returns aggregated rows from the lifestyle_summary view for the given user
     * with optional month range filters.
     */
    List<LifestyleSummary> summary(UUID userId, LocalDate monthFrom, LocalDate monthTo);

    /**
     * Returns paginated lifestyle-tagged transactions for the given user
     * with optional category and date filters.
     */
    PageTransaction listTransactions(Integer page, Integer size, String sort,
                                     UUID userId, Integer categoryId,
                                     OffsetDateTime dateFrom, OffsetDateTime dateTo);
}
