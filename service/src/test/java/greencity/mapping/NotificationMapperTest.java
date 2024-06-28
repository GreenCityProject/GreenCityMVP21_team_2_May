package greencity.mapping;

import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationUserDto;
import greencity.entity.User;
import greencity.entity.notification.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationMapperTest {

    @InjectMocks
    private NotificationMapper mapper;

    @Mock
    Notification notification;

    @Mock
    User sender, user;

    @Test
    void convertTest() {
        // Mocking sender User behavior
        when(sender.getId()).thenReturn(1L);
        when(sender.getName()).thenReturn("Test Sender");
        NotificationUserDto expectedSender = NotificationUserDto.builder()
                .id(sender.getId())
                .name(sender.getName())
                .build();

        // Mocking User behavior
        when(user.getId()).thenReturn(2L);
        when(user.getName()).thenReturn("Test User");
        NotificationUserDto expectedUser = NotificationUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();

        // Mocking Notification behavior
        when(notification.getId()).thenReturn(3L);
        when(notification.getUser()).thenReturn(user);
        when(notification.getSender()).thenReturn(sender);
        when(notification.getSection()).thenReturn("Test Section");
        when(notification.getMessage()).thenReturn("Test Message");
        when(notification.getTimestamp()).thenReturn(LocalDateTime.now());
        when(notification.isRead()).thenReturn(true);

        NotificationDto result = mapper.convert(notification);

        assertEquals(notification.getId(), result.getId());
        assertEquals(expectedUser, result.getUser());
        assertEquals(expectedSender, result.getSender());
        assertEquals(notification.getSection(), result.getSection());
        assertEquals(notification.getMessage(), result.getMessage());
        assertEquals(notification.getTimestamp(), result.getTimestamp());
        assertEquals(notification.isRead(), result.isRead());
    }
}