package com.melhamra.api.exceptions;


import com.melhamra.api.responses.ErrorMessage;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<Object> userExceptionHandler(UserException ex, WebRequest rq){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setTimestamp(new Date());
        errorMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> userExceptionHandlerForDataValidationError(MethodArgumentNotValidException ex, WebRequest rq){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> othersExceptionHandler(Exception ex, WebRequest rq){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setTimestamp(new Date());
        errorMessage.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

}
