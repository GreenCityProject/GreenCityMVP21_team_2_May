package greencity.mapping;

import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationUserDto;
import greencity.entity.User;
import greencity.entity.notification.Notification;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

/**
 * Class that used by {@link org.modelmapper.ModelMapper} to map {@link Notification} into
 * {@link NotificationDto}.
 */
@Component
public class NotificationMapper extends AbstractConverter<Notification, NotificationDto> {
    /**
     * Method convert {@link Notification} to {@link NotificationDto}.
     *
     * @param notification The notification entity to convert.
     * @return {@link NotificationDto}
     */
    @Override
    protected NotificationDto convert(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .user(convertUserToNotificationUserDto(notification.getUser()))
                .sender(convertUserToNotificationUserDto(notification.getSender()))
                .section(notification.getSection())
                .message(notification.getMessage())
                .timestamp(notification.getTimestamp())
                .isRead(notification.isRead())
                .build();
    }

    /**
     * Helper method to convert a {@link User} entity into a {@link NotificationUserDto}.
     * @param user The user entity to convert.
     * @return {@link NotificationUserDto}
     */
    private NotificationUserDto convertUserToNotificationUserDto(User user) {
        return NotificationUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
