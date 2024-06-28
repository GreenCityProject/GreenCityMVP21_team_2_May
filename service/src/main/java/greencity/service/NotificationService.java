package greencity.service;

import greencity.dto.notification.NotificationDto;
import greencity.entity.notification.Notification;
import greencity.exception.exceptions.NotificationNotFoundException;
import greencity.repository.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final ModelMapper modelMapper;
    private final NotificationRepo notificationRepo;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadNotifications(Long userId) {
        List<Notification> notifications = notificationRepo.findByUserIdAndIsReadFalse(userId);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepo.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getAllNotifications(Long userId) {
        List<Notification> notifications = notificationRepo.findByUserId(userId);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }
}

