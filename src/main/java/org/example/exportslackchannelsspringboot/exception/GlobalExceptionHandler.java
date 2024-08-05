package org.example.exportslackchannelsspringboot.exception;

import com.slack.api.methods.SlackApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

//TODO are these runtime exceptoin handler
// catch (IOException e) {
//        throw new RuntimeException(e);
//// Throwing this exception indicates your app or Slack servers had a connectivity issue.
//        } catch (SlackApiException e) {
//        throw new RuntimeException(e);
// Slack API responded with unsuccessful status code (= not 20x)

// TODO i will look into this - these are notes i copied from GA bootcamp supplemtnary video
@ControllerAdvice
public class GlobalExceptionHandler {


//    method slackMethods.usersConversations may throw an IOException if there is an issue with the network communication or data transfer when making the API call; this was recommend to add in Slack API documentation
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service unavailable. Please try again later.");
    }

    // Common Causes: Occurs when there is an error in making an API request to Slack, such as invalid parameters, authentication issues, or other bad requests.
    @ExceptionHandler(SlackApiException.class)
    public ResponseEntity<String> handleSlackApiException(SlackApiException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There was error in making API request to Slack. Slack is Slacking off today.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred. Please try again later.");
    }

}
