package com.team7.controller;

import com.team7.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testSendMessage() {
        String message = "Test message";
        notificationService.sendMessage(message);

        verify(restTemplate).postForObject(any(String.class), any(), any());
    }
}