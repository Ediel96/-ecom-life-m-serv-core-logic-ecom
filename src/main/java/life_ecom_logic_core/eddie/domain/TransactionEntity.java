// java
// File: 'src/main/java/life_ecom_logic_core/eddie/domain/TransactionEntity.java'
package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import life_ecom_logic_core.eddie.converter.TransactionTypeConverter;
import lombok.*;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.annotations.JdbcType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Convert(converter = TransactionTypeConverter.class)
    @Column(name = "transaction_type", columnDefinition = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

    @Column(name = "description")
    private String description;

    @Column(name = "date", nullable = false)
    private OffsetDateTime date;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // New fields
    @Column(name = "notification_date")
    private LocalDate notificationDate;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "frequency", columnDefinition = "frequency_type")
    private FrequencyType frequency;

    @Column(name = "lifestyle", nullable = false)
    private boolean lifestyle;

    @Column(name = "notification", nullable = false)
    private boolean notification;
}
