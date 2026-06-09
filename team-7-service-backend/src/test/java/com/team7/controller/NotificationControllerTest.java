package com.team7.controller;

import com.team7.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void testSendSlackMessage() throws Exception {
        String message = "Hello, Slack!";

        mockMvc.perform(MockMvcRequestBuilders.post("/send-slack-message")
                        .content(message)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(notificationService).sendMessage(message);
    }
}