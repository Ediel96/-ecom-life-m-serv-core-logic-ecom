package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * JPA entity for the recurring_transactions table.
 * Stores a rule that auto-generates transactions on a schedule via
 * the process_recurring_transactions() PostgreSQL function.
 */
@Entity
@Table(name = "recurring_transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecurringTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Template transaction whose amount/category/account are copied on each run. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionEntity transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "frequency", columnDefinition = "frequency_type", nullable = false)
    private FrequencyType frequency;

    /** Number of frequency units between executions (e.g. 2 + month = every 2 months). */
    @Column(name = "interval_count", nullable = false)
    private int intervalCount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Null means the rule runs indefinitely. */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** Date of the next scheduled execution. Updated automatically after each run. */
    @Column(name = "next_due_date", nullable = false)
    private LocalDate nextDueDate;

    @Column(name = "last_processed_date")
    private LocalDate lastProcessedDate;

    /** Days before next_due_date to send a payment_reminder notification. */
    @Column(name = "notification_days_before")
    private int notificationDaysBefore;

    /** Field named 'active' so Lombok generates isActive() correctly. Maps to is_active column. */
    @Column(name = "is_active", nullable = false)
    private boolean active;

    /** When true, generated transactions inherit lifestyle = true. Maps to is_lifestyle column. */
    @Column(name = "is_lifestyle", nullable = false)
    private boolean lifestyle;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
