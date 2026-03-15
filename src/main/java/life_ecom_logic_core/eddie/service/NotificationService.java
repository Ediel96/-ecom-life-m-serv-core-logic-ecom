package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.Notification;
import com.backend.organize_life.model.NotificationType;
import com.backend.organize_life.model.PageNotification;

import java.util.UUID;

public interface NotificationService {

    PageNotification list(Integer page, Integer size, String sort,
                          UUID userId, Boolean isRead, NotificationType type);

    Notification get(Long id);

    /** Sets is_read = true on a single notification. Returns null if not found. */
    Notification markRead(Long id);

    /** Marks all unread notifications for the user as read. Returns count updated. */
    int markAllRead(UUID userId);

    boolean delete(Long id);
}
