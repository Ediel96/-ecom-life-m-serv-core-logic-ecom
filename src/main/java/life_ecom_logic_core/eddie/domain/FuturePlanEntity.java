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
public class FuturePlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UserEntity user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "target_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "priority")
    private String priority; // HIGH, MEDIUM, LOW

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE, COMPLETED, PAUSED

    @Column(name = "icon", length = 50)
    private String icon; // e.g. MOTORCYCLE, HOUSE, CAR, TRAVEL

    @Column(name = "reminder_type", length = 20)
    private String reminderType; // e.g. MONTHLY, WEEKLY

    @Column(name = "reminder_day")
    private Integer reminderDay; // e.g. 1 = 1st of month

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<PlanMovementEntity> movements;
}
