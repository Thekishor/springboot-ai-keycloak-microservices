package com.ai_service.exception;

public class ActivityNotFoundException extends RuntimeException{

    public ActivityNotFoundException(String msg) {
        super(msg);
    }
}
