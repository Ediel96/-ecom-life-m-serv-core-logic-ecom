package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import life_ecom_logic_core.eddie.converter.TransactionTypeConverter;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * JPA entity for the recurring_transactions table.
 * Stores the rule config AND the template data used to generate each transaction.
 * Generated transactions reference back via transactions.recurring_transaction_id.
 */
@Entity
@Table(name = "recurring_transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecurringTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid", nullable = false)
    private UserEntity user;

    // ── Template data (what gets copied into each generated transaction) ───────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Convert(converter = TransactionTypeConverter.class)
    @Column(name = "transaction_type", columnDefinition = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

    // ── Schedule config ───────────────────────────────────────────────────────

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

    /** Maps to is_active column. Lombok generates isActive() for boolean field named 'active'. */
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
