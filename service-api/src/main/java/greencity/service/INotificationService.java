package greencity.service;

import greencity.dto.notification.NotificationDto;

import java.util.List;

public interface INotificationService {

    /**
     * Retrieves a list of unread notifications for a given user.
     *
     * @param userId the ID of the user whose notifications are to be fetched
     * @return a list of NotificationDTO objects representing unread notifications
     */
    List<NotificationDto> getUnreadNotifications(Long userId);

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId the ID of the notification to mark as read
     */
    void markNotificationAsRead(Long notificationId);

    /**
     * Retrieves all notifications for a given user.
     *
     * @param userId the ID of the user whose notifications are to be fetched
     * @return a list of NotificationDTO objects representing all notifications
     */
    List<NotificationDto> getAllNotifications(Long userId);
}

