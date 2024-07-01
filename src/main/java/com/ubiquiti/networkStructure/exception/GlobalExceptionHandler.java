package com.ubiquiti.networkStructure.exception;

import com.ubiquiti.networkStructure.dto.Status;
import com.ubiquiti.networkStructure.dto.StatusCode;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Global Exception Handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles IllegalArgumentException.
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Status> handleIllegalArgumentException(IllegalArgumentException exception) {
        Status status = new Status();
        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription(exception.getMessage());
        return ResponseEntity.badRequest().body(status);
    }

    /**
     * Handles JdbcSQLIntegrityConstraintViolationException.
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler({JdbcSQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Status> handleConstraintViolationException(JdbcSQLIntegrityConstraintViolationException exception) {
        Status status = new Status();
        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription("MAC is globally unique, so two devices can't have the same MAC address");
        return ResponseEntity.badRequest().body(status);
    }

    /**
     * Handles NetworkDeviceNotFoundException.
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler({NetworkDeviceNotFoundException.class})
    public ResponseEntity<Status> handleNetworkDeviceNotFoundException(NetworkDeviceNotFoundException exception) {
        Status status = new Status();
        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription(exception.getMessage());
        return ResponseEntity.badRequest().body(status);
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Status> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Status status = new Status();
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription(errors.toString());
        return ResponseEntity.badRequest().body(status);
    }
}
