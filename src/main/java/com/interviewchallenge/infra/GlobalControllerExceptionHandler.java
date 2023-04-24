package com.interviewchallenge.infra;

import com.interviewchallenge.exception.ErrorResponse;
import com.interviewchallenge.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse("commonError", "commonMessageError");
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) exception;
            errorResponse = new ErrorResponse(serviceException.getInternalErrorCode(), serviceException.getMessage());
            errorResponse.setMessageInformation(exception.getMessage());
            httpStatus = serviceException.getStatus();
        } else {
            log.error("Exception encountered. ", exception);
        }
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
