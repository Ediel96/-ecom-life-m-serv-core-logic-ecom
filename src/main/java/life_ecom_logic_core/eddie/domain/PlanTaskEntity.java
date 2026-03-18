package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "plan_tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private FuturePlanEntity plan;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "value", nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
