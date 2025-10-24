package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGSERIAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account; // optional

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "uuid")
    private UserEntity user; // optional

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category; // nullable (ON DELETE SET NULL)

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, columnDefinition = "transaction_type")
    private TransactionTypeEnum transactionType;

    @Column(name = "description")
    private String description;

    @Column(name = "date", nullable = false)
    private OffsetDateTime date; // TIMESTAMPTZ

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt; // TIMESTAMPTZ

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt; // TIMESTAMPTZ
}