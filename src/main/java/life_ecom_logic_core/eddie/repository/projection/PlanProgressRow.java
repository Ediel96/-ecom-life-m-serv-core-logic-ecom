package life_ecom_logic_core.eddie.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Spring Data projection for the plan_progress view.
 */
public interface PlanProgressRow {
    Integer    getId();
    UUID       getUserId();
    String     getTitle();
    String     getIcon();
    BigDecimal getTargetAmount();
    LocalDate  getTargetDate();
    String     getStatus();
    String     getReminderType();
    Integer    getReminderDay();
    BigDecimal getSaved();
    BigDecimal getProgressPct();
    BigDecimal getAvgMonthlySavings();
    LocalDate  getEstimatedCompletionDate();
}
