package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.NotificationEntity;
import life_ecom_logic_core.eddie.domain.NotificationTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
        select n from NotificationEntity n
        where n.user.id = :userId
          and (:isRead IS NULL OR n.read = :isRead)
          and (:type IS NULL OR n.type = :type)
        order by n.scheduledFor desc
        """)
    Page<NotificationEntity> search(
            @Param("userId") UUID userId,
            @Param("isRead") Boolean isRead,
            @Param("type") NotificationTypeEnum type,
            Pageable pageable
    );

    /** Marks all unread notifications as read for a given user. Returns the count updated. */
    @Modifying
    @Query("""
        update NotificationEntity n set n.read = true
        where n.user.id = :userId and n.read = false
        """)
    int markAllReadByUserId(@Param("userId") UUID userId);
}
