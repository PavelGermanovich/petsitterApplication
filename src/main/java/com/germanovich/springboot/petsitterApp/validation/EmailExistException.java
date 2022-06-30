package com.germanovich.springboot.petsitterApp.validation;

public class EmailExistException extends Throwable{
    public EmailExistException(String message) {
        super(message);
    }
}
