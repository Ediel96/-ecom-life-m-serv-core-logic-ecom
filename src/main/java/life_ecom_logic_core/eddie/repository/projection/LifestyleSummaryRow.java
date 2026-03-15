package life_ecom_logic_core.eddie.repository.projection;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Spring Data projection interface for native queries against the lifestyle_summary view.
 * Property names must match the column aliases used in the @Query.
 */
public interface LifestyleSummaryRow {
    UUID getUserId();
    String getCategoryName();
    OffsetDateTime getMonth();
    Long getTransactionCount();
    Double getTotalAmount();
    Double getAverageAmount();
}
