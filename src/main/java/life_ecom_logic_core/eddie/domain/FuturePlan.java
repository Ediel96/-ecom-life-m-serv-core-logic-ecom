package life_ecom_logic_core.eddie.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "future_plans")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FuturePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // SERIAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "estimated_cost", precision = 12, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "priority")
    private String priority; // HIGH, MEDIUM, LOW

    @Column(name = "status")
    private String status; // PLANNING, SAVING, COMPLETED, CANCELLED

    @Column(name = "done")
    private Boolean done;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt; // TIMESTAMPTZ

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt; // TIMESTAMPTZ

    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<PlanMovement> movements;
}