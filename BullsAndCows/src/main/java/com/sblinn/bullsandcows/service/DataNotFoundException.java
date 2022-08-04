
package com.sblinn.bullsandcows.service;

/**
 *
 * @author Sara Blinn
 */
public class DataNotFoundException extends Exception {
 
    public DataNotFoundException(String message) {
        super(message);
    }
    
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
