package org.example.exportslackchannelsspringboot.controller;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import org.example.exportslackchannelsspringboot.service.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class SlackController {
    @Autowired
    SlackService slackService;
    // TODO save token to env varialbe



    // RequestParam:  Used for capturing values from query parameters in the URL.
    @GetMapping("/channels")

    //  resource link:  https://slack.dev/java-slack-sdk/guides/web-api-basics
    // here is how we build a request object- chaining the filters and then building, limiting it to x results (you can see full definition of argument filters here https://api.slack.com/methods/users.conversations)

    public ResponseEntity<String> getChannelsList(@RequestParam String userId) throws SlackApiException, IOException {
        String responseBodyChannelList = slackService.getChannelsList(userId);
        return ResponseEntity.ok(responseBodyChannelList);
    }



    //Open command can only be used to allow users to join pubilc channels
    @GetMapping("/join-channel-command")
    public ResponseEntity<String> getFormattedChannelsCommand(@RequestParam String userId) throws SlackApiException, IOException {
        // Call the service method; global exception handler will catch any exceptions
        String formattedChannels = slackService.getFormattedChannelsCommand(userId);
        return ResponseEntity.ok(formattedChannels);
    }
}



