package com.sellics.casestudy.exception.config;

import com.sellics.casestudy.exception.client.ResourceNotFoundException;
import com.sellics.casestudy.exception.server.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, ServerException.class})
    public ExceptionResponse exceptionHandler(Exception exception){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrMessage("Server Error");
        exceptionResponse.setThrowable(exception.getCause());
        return exceptionResponse;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionResponse resourceNotFoundExceptionHandler(ResourceNotFoundException exception){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrMessage(exception.getMessage());
        exceptionResponse.setThrowable(exception.getCause());
        return exceptionResponse;
    }
}
