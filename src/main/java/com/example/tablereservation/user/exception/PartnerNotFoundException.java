package com.example.tablereservation.user.exception;

public class PartnerNotFoundException extends RuntimeException {
    public PartnerNotFoundException(String message){
        super(message);
    }
}
