package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.Notification;
import com.backend.organize_life.model.NotificationType;
import com.backend.organize_life.model.PageNotification;
import life_ecom_logic_core.eddie.domain.NotificationEntity;
import life_ecom_logic_core.eddie.repository.NotificationRepository;
import life_ecom_logic_core.eddie.service.NotificationService;
import life_ecom_logic_core.eddie.service.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final int DEFAULT_PAGE      = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final NotificationRepository notificationRepository;
    private final NotificationMapper     mapper;

    @Override
    public PageNotification list(Integer page, Integer size, String sort,
                                  UUID userId, Boolean isRead, NotificationType type) {
        log.debug("Listing notifications - userId: {}, isRead: {}", userId, isRead);

        int pageNum  = normalize(page);
        int pageSize = normalizeSize(size);
        Pageable pageable = PageRequest.of(pageNum, pageSize, buildSort(sort));

        life_ecom_logic_core.eddie.domain.NotificationTypeEnum domainType =
                mapper.mapTypeToDomain(type);

        Page<NotificationEntity> result =
                notificationRepository.search(userId, isRead, domainType, pageable);

        return buildPage(result, pageNum, pageSize);
    }

    @Override
    public Notification get(Long id) {
        log.debug("Fetching notification id: {}", id);
        return notificationRepository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public Notification markRead(Long id) {
        log.info("Marking notification {} as read", id);
        return notificationRepository.findById(id)
                .map(entity -> {
                    entity.setRead(true);
                    if (entity.getSentAt() == null) {
                        entity.setSentAt(OffsetDateTime.now());
                    }
                    return mapper.toDto(notificationRepository.save(entity));
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public int markAllRead(UUID userId) {
        log.info("Marking all notifications as read for userId: {}", userId);
        return notificationRepository.markAllReadByUserId(userId);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting notification id: {}", id);
        if (!notificationRepository.existsById(id)) return false;
        notificationRepository.deleteById(id);
        return true;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private int normalize(Integer page) {
        return (page == null || page < 0) ? DEFAULT_PAGE : page;
    }

    private int normalizeSize(Integer size) {
        return (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size;
    }

    private Sort buildSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) return Sort.by(Sort.Direction.DESC, "scheduledFor");
        String[] parts = sortParam.split(",");
        String field  = parts[0].trim();
        Sort.Direction dir = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    private PageNotification buildPage(Page<NotificationEntity> page, int pageNum, int pageSize) {
        List<Notification> content = page.getContent().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        PageNotification response = new PageNotification();
        response.setContent(content);
        response.setPage(pageNum);
        response.setSize(pageSize);
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
