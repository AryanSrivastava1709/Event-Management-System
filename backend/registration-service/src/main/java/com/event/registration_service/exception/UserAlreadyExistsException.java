package com.event.registration_service.exception;


//This is a custom defined exception class which is used to handle the user already exists exception

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}