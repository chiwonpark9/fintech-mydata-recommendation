package com.team7.service.notification;

import com.slack.api.methods.SlackApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.slack.api.Slack;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import java.io.IOException;


@Service
public class NotificationService {

    @Value("${slack.token}")
    private String slackToken;

    @Value("${slack.channel.id}")
    private String channelId;

    private final RestTemplate restTemplate;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String message) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(slackToken);
//
//        String body = String.format("{\"channel\":\"%s\",\"text\":\"%s\"}", channelId, message);
//
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//

        Slack slack = Slack.getInstance();
        try {
            ChatPostMessageResponse response = slack.methods(slackToken).chatPostMessage((req->req
                    .channel(channelId)
                    .text(message)));
            System.out.println("Response from Slack: " + response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }
    }
}
