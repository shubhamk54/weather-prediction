package com.sapient.backend.exception;

import com.sapient.backend.model.RestErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RestExceptionAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionAdvice.class);

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<RestErrorResponse> handleNoDataError(DataNotFoundException ex) {
        LOG.error("DataNotFoundException occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new RestErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InternalServerException.class})
    public ResponseEntity<RestErrorResponse> handleInternalServerError(InternalServerException ex) {
        LOG.error("InternalServerException occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new RestErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<RestErrorResponse> handleValidationError(ValidationException ex) {
        LOG.error("ValidationException occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new RestErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<RestErrorResponse> handleInvalidInputError(MissingServletRequestParameterException ex) {
        LOG.error("IllegalArgumentException occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new RestErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
