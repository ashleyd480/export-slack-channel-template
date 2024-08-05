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

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        // Log the exception if necessary
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service unavailable. Please try again later.");
    }

    @ExceptionHandler(SlackApiException.class)
    public ResponseEntity<String> handleSlackApiException(SlackApiException e) {
        // Log the exception if necessary
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request. Please check your parameters.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        // Log the exception if necessary
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal server error occurred. Please try again later.");
    }

}
