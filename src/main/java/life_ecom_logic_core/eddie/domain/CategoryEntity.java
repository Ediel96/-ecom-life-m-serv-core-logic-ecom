package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // SERIAL

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "icon")
    private String icon;

    @Column(name = "color_fill")
    private String colorFill;

    @Column(name = "color_bg")
    private String colorBg;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "transaction_type", columnDefinition = "transaction_type", nullable = false)
    private TransactionTypeEnum transactionType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt; // TIMESTAMPTZ

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt; // TIMESTAMPTZ

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<TransactionEntity> transactions;
}