package org.example.exportslackchannelsspringboot.service;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.users.UsersConversationsRequest;
import com.slack.api.methods.response.users.UsersConversationsResponse;
import com.slack.api.model.ConversationType;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SlackService {


    // You can edit your own token in environmental variables TOKEN=asdfj-12341234  OR you can just add its value to after the TOKEN = sign
    String TOKEN =  System.getenv("TOKEN");

    // Initialize an API Methods client with the given token
    // use private final to ensure that these "specific instances" stay constant inside the class
// Use private final to ensure that these "specific instances" stay constant inside the class

    private final Slack slack = Slack.getInstance(); // to initialize SLack object; responsible for creating an instance of the Slack API client. This client is used to interact with Slack’s API.

    private final MethodsClient slackMethods = slack.methods(TOKEN);



//TODO when you utilize this, make sure to adjust your limit, i.e. say you have 100 channels, increase the limit
    public String getChannelsList(String userId) throws SlackApiException, IOException {
        UsersConversationsResponse response = slackMethods.usersConversations(UsersConversationsRequest.builder()
                .user(userId)
                .types(List.of(ConversationType.PUBLIC_CHANNEL, ConversationType.PRIVATE_CHANNEL))
                .limit(200)
                .build());
// ^ initially tried setting .exclude_archived(true) but it was not recognizing the method so will filter in stream api


        // Define the header row
        String header = "channel_name,private_or_public,description";

// Collect the channel data
        String dataRows = response.getChannels().stream()
                .filter(channel -> !channel.isArchived())
                .map(channel -> String.join(",",
                        channel.getName(),
                        channel.isPrivate() ? "Private" : "Public",
                        channel.getPurpose() != null ? "\"" + Jsoup.parse(channel.getPurpose().getValue()).text() + "\"" : "\"\""
                        // Wrapping description in quotes and handling null with empty quotes
                ))
                .collect(Collectors.joining("\n")); // Join with newline character

// Combine header and data rows
        return header + "\n" + dataRows;
    }

    public String getFormattedChannelsCommand(String userId) throws IOException, SlackApiException {
        UsersConversationsResponse response = slackMethods.usersConversations(UsersConversationsRequest.builder()
                .user(userId)
                .types(List.of(ConversationType.PUBLIC_CHANNEL))
                .limit(10)
                .build());

        return "/Open " + response.getChannels().stream()
                .filter(channel -> !channel.isArchived())
                .map(channel -> "#" + channel.getName())
                .collect(Collectors.joining(" "));
    }
}
// collect(Collectors.joining("\n")): This collects the elements of the stream into a single String, with each element separated by the specified delimiter (in this case, a newline character).





