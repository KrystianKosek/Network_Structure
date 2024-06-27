package com.ubiquiti.networkStructure.exception;

import com.ubiquiti.networkStructure.dto.Status;
import com.ubiquiti.networkStructure.dto.StatusCode;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Status> handleIllegalArgumentException(IllegalArgumentException exception) {
        Status status = new Status();
        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription(exception.getMessage());
        return ResponseEntity.badRequest().body(status);
    }

    @ExceptionHandler({JdbcSQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Status> handleConstraintViolationException(JdbcSQLIntegrityConstraintViolationException exception) {
        Status status = new Status();
        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription("MAC is globally unique, so two devices can't have the same MAC address");
        return ResponseEntity.badRequest().body(status);
    }

    @ExceptionHandler({NetworkDeviceNotFoundException.class})
    public ResponseEntity<Status> handleNetworkDeviceNotFoundException(NetworkDeviceNotFoundException exception) {
        Status status = new Status();
        status.setStatusCode(StatusCode.ERROR);
        status.setStatusDescription(exception.getMessage());
        return ResponseEntity.badRequest().body(status);
    }
}
