package greencity.controller;

import greencity.dto.notification.NotificationDto;
import greencity.service.INotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private static final String baseURL = "/notifications";
    private MockMvc mockMvc;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(notificationController)
                .build();
    }

    @Test
    void getUnreadNotifications_ReturnsOk() throws Exception {
        when(notificationService.getUnreadNotifications(anyLong()))
                .thenReturn(Collections.singletonList(new NotificationDto()));
        mockMvc.perform(get(baseURL + "/unread/{userId}", 1))
                .andExpect(status().isOk());
        verify(notificationService).getUnreadNotifications(1L);
    }

    @Test
    void getUnreadNotifications_ReturnsNotFound() throws Exception {
        mockMvc.perform(get(baseURL + "/unread/{userId}", "not_number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void markNotificationAsRead_ReturnsOk() throws Exception {
        mockMvc.perform(post(baseURL + "/read/{notificationId}", 1))
                .andExpect(status().isOk());
        verify(notificationService).markNotificationAsRead(1L);
    }

    @Test
    void markNotificationAsRead_ReturnsNotFound() throws Exception {
        mockMvc.perform(post(baseURL + "/read/{notificationId}", "not_number"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllNotifications_ReturnsOk() throws Exception {
        when(notificationService.getAllNotifications(anyLong()))
                .thenReturn(Collections.singletonList(new NotificationDto()));
        mockMvc.perform(get(baseURL + "/all/{userId}", 1))
                .andExpect(status().isOk());
        verify(notificationService).getAllNotifications(1L);
    }

    @Test
    void getAllNotifications_ReturnsNotFound() throws Exception {
        mockMvc.perform(get(baseURL + "/all/{userId}", "not_number"))
                .andExpect(status().isBadRequest());
    }
}