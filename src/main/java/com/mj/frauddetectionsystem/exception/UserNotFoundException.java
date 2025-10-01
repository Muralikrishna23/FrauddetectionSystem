package com.mj.frauddetectionsystem.exception;


public class UserNotFoundException extends FraudDetectionException {
    
    private String userId;

    public UserNotFoundException(String userId) {
        super("USER_NOT_FOUND", String.format("User not found with ID: %s", userId));
        this.userId = userId;
    }

    public UserNotFoundException(String userId, String message) {
        super("USER_NOT_FOUND", message);
        this.userId = userId;
    }

    public UserNotFoundException(String userId, String message, Throwable cause) {
        super("USER_NOT_FOUND", message, cause);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
