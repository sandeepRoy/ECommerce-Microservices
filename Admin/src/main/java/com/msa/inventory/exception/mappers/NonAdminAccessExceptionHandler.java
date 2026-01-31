package com.msa.inventory.exception.mappers;

import com.msa.inventory.exception.custom.NonAdminAccessException;
import com.msa.inventory.exception.info.NonAdminAccessExceptionInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NonAdminAccessExceptionHandler {
    @ExceptionHandler(value = NonAdminAccessException.class)
    public ResponseEntity<NonAdminAccessExceptionInfo> handleLoginException(NonAdminAccessException nonAdminAccessException) {
        NonAdminAccessExceptionInfo exceptionInformation = new NonAdminAccessExceptionInfo();
        exceptionInformation.setMessage(nonAdminAccessException.getMessage());
        exceptionInformation.setCode("ADMIN-INVALID-ACCESS");
        return new ResponseEntity<>(exceptionInformation, HttpStatus.UNAUTHORIZED);
    }
}
