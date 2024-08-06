
# Export Slack Channels Template 
---

# Intro

This mini-project was created in my 6th month of coding, using Java and Spring Boot. It allowed me to delve into working with an existing API—in this case, the Slack API. The project enables users to:

- Fetch a list of Slack channels: Retrieve both private and public Slack channels that the user is a part of along with details. This list can then be shared with new hires or apprentices as an overview of the relevant channels. 

- Generate a shareable Slack command: Create a Slack command that new hires or apprentices can use to quickly join the relevant public channels. This streamlines the onboarding process by providing a convenient way to direct new team members to the essential channels they need to join.

Note: Due to the time constraint of less than 3 days to work on this, this mini-project is in an MVP (Minimum Viable Product) stage and serves as a functioning template to demonstrate the core functionality. It provides a template that can be expanded upon. 


---

# Quick Setup Guide

1. **Clone the Repository:**

   - Use `git clone` to copy the code to your local desktop. For an optimal development experience, it’s recommended to use IntelliJ as your IDE.

2. **Create a Slack App:**

   - Go to the [Slack API website](https://api.slack.com/apps) and create a new web app. This step is crucial for generating a token that your application will use to interact with Slack.

3. **Generate Your Token:**

   - **Navigate to Slack API:** Visit the Slack API Apps page and click on "Create New App."
   - **Choose App Type:** Select "From scratch" and provide a name for your app. Choose the Slack workspace where you want to install this app.
   - **Configure OAuth & Permissions:** Go to the "OAuth & Permissions" section in the app configuration. Here, you’ll need to set up the required scopes:
     - **Bot Token Scopes:** Add `channels:read` to allow the bot to read information about public channels, and `groups:read` to access private channels.
     - **User Token Scope:** Add `channels:read` to allow the app to filter channels by user.
   - **Install App to Workspace:** Click on "Install App to Workspace" to generate a token. You’ll be redirected to a Slack authorization page where you need to authorize your app. After authorization, you’ll receive a token which is used for API calls.
   - **Copy the Token:** Save this token securely as you’ll need to configure it in your development environment.

4. **Configure Token Scopes:**

   - **Assign the Following Scopes to Your Token:**
     - **Bot Token:** `channels:read` and `groups:read` to allow the bot to access both public and private channels.
     - **User Token Scope:** `channels:read` to enable filtering channels based on the user.

5. **Set Up Environment Variables:**

   - Copy and paste the generated token into IntelliJ as an environment variable. This step ensures that your token is securely used by the application without hardcoding it into the source code.

6. **Adjust Channel Limits:**

   - In the `SlackService` file, update the `.limit()` values to accommodate the number of channels you have. For instance, if you have around 180 channels (give or take), set the limit to something higher like 200. Make sure to update the limit for both methods: `getChannelsList` and `getFormattedChannels`. This adjustment ensures that you retrieve all relevant channels without missing any due to limit constraints.

    ```
    public String getChannelsList(String userId) throws SlackApiException, IOException {
        UsersConversationsResponse response = slackMethods.usersConversations(UsersConversationsRequest.builder()
                .user(userId)
                .types(List.of(ConversationType.PUBLIC_CHANNEL, ConversationType.PRIVATE_CHANNEL))
                .limit(200) 
                .build());
    }
    ```

7. **Find Your User ID:**

   - To obtain your Slack user ID, click on your profile photo at the bottom left of the Slack desktop app. Select "Profile" from the pop-up, which opens your profile on the right side. Click the three dots next to "Set a status" and select "Copy member ID." Save this ID in a secure spot as you will need it for API requests.

8. **Test the Endpoints:**

   - Use Postman or your web browser to access the following URLs:
     - To get a list of channels: `http://localhost:8080/join-channel-command?userId=[your_member_id]`
     - To get the formatted channel command: `http://localhost:8080/join-channel-command?userId=[your_member_id]`
   - Replace `[your_member_id]` with the user ID you copied earlier.

9. **Review the Response:**

   - **Example Response for List of Channels:**
     ```
     channel_name,private_or_public,description
     marketing-trends,Public,"Discusses the latest trends and strategies in marketing."
     dev-discussions,Public,"A space for developers to share ideas and code snippets."
     customer-support,Public,"For customer support team to handle inquiries and provide solutions."
     data-insights,Public,"Discusses insights and analysis of data collected across various projects."
     team-culture,Public,"A channel for team-building activities and sharing company culture."
     ```

   - **Example Response for Formatted Channel Command:**
     ```
     /Open #marketing-trends #dev-discussions #customer-support #data-insights #team-culture
     ```

10. **Format in Google Sheets:**

    - Copy the response data and paste it into a blank Google Sheet. Click on the clipboard icon that appears to split text into columns. This will organize the data into separate columns for channel name, type, and description.

11. **Filter Your List:**

    - Use Google Sheets’ filter options to further refine your list by channel description. For example, if you want to share channels related to "support" with new hires, you can filter by description to find relevant channels.

12. **Use the /open Command:**

    - To facilitate new hires joining relevant channels, share the `/Open` command with them. They can copy and paste this command to quickly join the channels you’ve identified. Currently this command on Slack is only compatible with public channels. 


---

# Functionality

# Methods

1. **getChannelsList**
This method sends a request to Slack API to get list of public and private channels based on member (user). Archived channels are excluded. 
The API response includes a list of channels which is then streamed - and for each channel, we retrieve the name, private or public, and the channel purpose (description).  
Note: channel purpose had to be wrapped in double quotes, to handle cases where the description might contain commas or other special characters that could interfere with CSV formatting.

2. **getFormattedChannelsCommand**
This method sends a request to Slack API to get a list of public channels based on your member (user) id, since Slack only allow the `/Open` command to be used with public channels. 
The channel data is then processsed and formatted it into a single Slack `/Open` command string.


# Libraries (Dependencies)

The following libraries are used in this project: 

## Slack API Client 
This library simplifies interactions with the Slack API, handling the nitty-gritty of HTTP requests and JSON processing. It abstracts away the complexity of making network calls, allowing you to focus on implementing the functionality you need rather than dealing with the lower-level details of the API. By managing authentication, request formatting, and response parsing,

```
<dependency>
    <groupId>com.slack.api</groupId>
    <artifactId>slack-api-client</artifactId>
    <version>1.20.0</version>
</dependency>
```

## Jsoup
Jsoup helps parse and manipulate HTML and can handle various HTML formatting quirks. 
```
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.16.1</version>
</dependency>
```

For example, this code block below checks if the channel’s purpose is not null. If it’s present, it uses Jsoup to parse and clean the HTML content of the channel’s purpose, ensuring any HTML tags are stripped out and only the text remains. 

```
channel.getPurpose() != null ? "\"" + Jsoup.parse(channel.getPurpose().getValue()).text() + "\"" : "\"\""
```
