package me.silvernine.jwttutorial.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException(String message) {
        super(message);
    }
}
