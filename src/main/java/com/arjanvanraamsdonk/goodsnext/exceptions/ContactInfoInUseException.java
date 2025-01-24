package com.arjanvanraamsdonk.goodsnext.exceptions;

public class ContactInfoInUseException extends RuntimeException {
    public ContactInfoInUseException(String message) {
        super(message);
    }
}
