
package com.sblinn.bullsandcows.service;

/**
 *
 * @author Sara Blinn
 */
public class DuplicatePrimaryKeyException extends Exception {

    public DuplicatePrimaryKeyException(String message) {
        super(message);
    }

    public DuplicatePrimaryKeyException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
