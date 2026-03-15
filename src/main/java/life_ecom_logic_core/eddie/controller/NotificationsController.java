package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.NotificationsApiDelegate;
import com.backend.organize_life.model.Notification;
import com.backend.organize_life.model.NotificationType;
import com.backend.organize_life.model.NotificationsReadAllPatch200Response;
import com.backend.organize_life.model.PageNotification;
import life_ecom_logic_core.eddie.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Controller delegate for Notification API operations.
 * Implements the NotificationsApiDelegate interface generated from OpenAPI spec.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationsController implements NotificationsApiDelegate {

    private final NotificationService notificationService;

    /** GET /notifications — userId is first because it is required in the spec. */
    @Override
    public ResponseEntity<PageNotification> notificationsGet(
            UUID userId,
            Integer page,
            Integer size,
            String sort,
            Boolean isRead,
            NotificationType type,
            OffsetDateTime scheduledBefore) {

        log.debug("Listing notifications - userId: {}, isRead: {}", userId, isRead);
        PageNotification result = notificationService.list(page, size, sort, userId, isRead, type);
        return ResponseEntity.ok(result);
    }

    /** GET /notifications/{id} */
    @Override
    public ResponseEntity<Notification> notificationsIdGet(Long id) {
        log.debug("Fetching notification id: {}", id);
        Notification result = notificationService.get(id);
        if (result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    /** DELETE /notifications/{id} */
    @Override
    public ResponseEntity<Void> notificationsIdDelete(Long id) {
        log.info("Deleting notification id: {}", id);
        try {
            if (notificationService.delete(id)) return ResponseEntity.noContent().build();
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Error deleting notification id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** PATCH /notifications/{id}/read */
    @Override
    public ResponseEntity<Notification> notificationsIdReadPatch(Long id) {
        log.info("Marking notification {} as read", id);
        Notification updated = notificationService.markRead(id);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    /** PATCH /notifications/read-all */
    @Override
    public ResponseEntity<NotificationsReadAllPatch200Response> notificationsReadAllPatch(UUID userId) {
        log.info("Marking all notifications as read for userId: {}", userId);
        int updated = notificationService.markAllRead(userId);
        NotificationsReadAllPatch200Response response = new NotificationsReadAllPatch200Response();
        response.setUpdated(updated);
        return ResponseEntity.ok(response);
    }
}
