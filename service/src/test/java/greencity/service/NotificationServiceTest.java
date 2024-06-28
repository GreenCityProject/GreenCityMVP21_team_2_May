package greencity.service;

import greencity.ModelUtils;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationUserDto;
import greencity.entity.User;
import greencity.entity.notification.Notification;
import greencity.exception.exceptions.NotificationNotFoundException;
import greencity.repository.NotificationRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationService notificationService;

    private final User user = ModelUtils.getUser(1L);
    private final User sender = ModelUtils.getUser(2L);
    private final String message = "testMessage";
    private final String section = "testSection";

    @Test
    public void getUnreadNotificationsTest() {
        Long userId = 1L;
        NotificationUserDto userDto = new NotificationUserDto(user.getId(), user.getName());
        NotificationUserDto senderDto = new NotificationUserDto(sender.getId(), sender.getName());

        Notification notification = ModelUtils.getNotification(1L, user, sender, section, message);
        NotificationDto dto = ModelUtils.getNotificationDto(1L, userDto, senderDto, section, message);

        when(notificationRepo.findByUserIdAndIsReadFalse(userId)).thenReturn(Collections.singletonList(notification));
        when(modelMapper.map(notification, NotificationDto.class)).thenReturn(dto);

        notificationService.getUnreadNotifications(userId);

        verify(notificationRepo).findByUserIdAndIsReadFalse(userId);
        verify(modelMapper).map(notification, NotificationDto.class);
    }

    @Test
    public void markNotificationAsRead_NotificationExists() {
        Long notificationId = 1L;
        Notification notification = ModelUtils.getNotification(1L, user, sender, section, message);

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationService.markNotificationAsRead(notificationId);

        verify(notificationRepo).save(notification);
    }

    @Test
    public void markNotificationAsRead_NotificationNotExist() {
        Long notificationId = 1L;

        when(notificationRepo.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class, () -> notificationService.markNotificationAsRead(notificationId));
    }

    @Test
    public void getAllNotificationsTest() {
        Long userId = 1L;
        NotificationUserDto userDto = new NotificationUserDto(user.getId(), user.getName());
        NotificationUserDto senderDto = new NotificationUserDto(sender.getId(), sender.getName());

        Notification notification = ModelUtils.getNotification(1L, user, sender, section, message);
        NotificationDto dto = ModelUtils.getNotificationDto(1L, userDto, senderDto, section, message);

        when(notificationRepo.findByUserId(userId)).thenReturn(Collections.singletonList(notification));
        when(modelMapper.map(notification, NotificationDto.class)).thenReturn(dto);

        notificationService.getAllNotifications(userId);

        verify(notificationRepo).findByUserId(userId);
        verify(modelMapper).map(notification, NotificationDto.class);
    }
}