package com.interviewchallenge.exception;

import org.springframework.http.HttpStatus;

public enum ServiceExceptionEnum {

    SESSION_NOT_FOUND("Session not found.", HttpStatus.NOT_FOUND),
    USER_STORY_NOT_FOUND("User story not found.", HttpStatus.NOT_FOUND),
    USER_STORY_ALREADY_VOTED("You've already voted this user story", HttpStatus.PRECONDITION_FAILED),
    USER_STORY_IS_NOT_VOTING("User story is not in VOTING status.", HttpStatus.PRECONDITION_FAILED),
    USER_STORY_IS_CANCELLED("User story is in CANCELLED status.", HttpStatus.PRECONDITION_FAILED),
    USER_STORY_NOT_VISIBLE("You are not allowed to see this user story.", HttpStatus.FORBIDDEN),
    USER_STORY_STATUS_CHANGE_NOT_ALLOWED("You are not allowed to change user story status.", HttpStatus.FORBIDDEN),
    USER_STORY_NOT_PRESENT_IN_SESSION("This user story is not present in this session.", HttpStatus.PRECONDITION_FAILED),
    USER_STORY_NOT_IN_CORRECT_STATUS("This user story is not is PENDING status. Cannot be removed from session.", HttpStatus.PRECONDITION_FAILED),
    SESSION_REMOVAL_NOT_ALLOWED("You are not allowed to remove the session.", HttpStatus.FORBIDDEN),
    SESSION_CANNOT_BE_JOINED("The session is not in ONGOING status.", HttpStatus.PRECONDITION_FAILED),
    MEMBER_ALREADY_IN_SESSION("Cannot join session. You are already in another session.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus code;

    ServiceExceptionEnum(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getCode() {
        return code;
    }
}
