package com.interviewchallenge.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException {

    private String internalErrorCode;
    private String message;
    private HttpStatus status;

    public ServiceException(ServiceExceptionEnum exceptionEnum) {
        this.internalErrorCode = exceptionEnum.name();
        this.message = exceptionEnum.getMessage();
        this.status = exceptionEnum.getCode();
    }

}
