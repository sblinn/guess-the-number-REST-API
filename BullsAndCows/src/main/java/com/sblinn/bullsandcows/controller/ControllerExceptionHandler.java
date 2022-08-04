
package com.sblinn.bullsandcows.controller;

import com.sblinn.bullsandcows.service.DataNotFoundException;
import com.sblinn.bullsandcows.service.DuplicatePrimaryKeyException;
import com.sblinn.bullsandcows.service.InvalidDataException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Sara Blinn
 */
@ControllerAdvice
@RestController
public class ControllerExceptionHandler         
        extends ResponseEntityExceptionHandler {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Error> handleSqlException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        final String CONSTRAINT_MESSAGE = 
            "Error: Could not save your input. "
            + "Please ensure it is valid and try again.";
        
        Error err = new Error();
        err.setMessage(CONSTRAINT_MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
   
    @ExceptionHandler(DuplicatePrimaryKeyException.class)
    public final ResponseEntity<Error> handleDuplicatePrimaryKeyException(
            DuplicatePrimaryKeyException ex,
            WebRequest request) {
        
        Error err = new Error();
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity<Error> handleDataNotFoundException(
            DataNotFoundException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NO_CONTENT);
    }
   
    @ExceptionHandler(InvalidDataException.class)
    public final ResponseEntity<Error> handleInvalidDataException(
            InvalidDataException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
