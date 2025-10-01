package com.mj.frauddetectionsystem.exception;

public class FraudDetectionException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final Object[] errorArgs;
    
    /**
     * Constructor with message only
     * @param message Error message
     */
    public FraudDetectionException(String message) {
        super(message);
        this.errorCode = "FRAUD_DETECTION_ERROR";
        this.errorArgs = null;
    }
    
    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause Root cause exception
     */
    public FraudDetectionException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "FRAUD_DETECTION_ERROR";
        this.errorArgs = null;
    }
    
    /**
     * Constructor with error code and message
     * @param errorCode Specific error code
     * @param message Error message
     */
    public FraudDetectionException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorArgs = null;
    }
    
    /**
     * Constructor with error code, message, and cause
     * @param errorCode Specific error code
     * @param message Error message
     * @param cause Root cause exception
     */
    public FraudDetectionException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorArgs = null;
    }
    
    /**
     * Constructor with error code, message, and arguments
     * @param errorCode Specific error code
     * @param message Error message template
     * @param errorArgs Arguments for message formatting
     */
    public FraudDetectionException(String errorCode, String message, Object... errorArgs) {
        super(String.format(message, errorArgs));
        this.errorCode = errorCode;
        this.errorArgs = errorArgs;
    }
    
    /**
     * Get the specific error code
     * @return Error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Get error arguments
     * @return Error arguments array
     */
    public Object[] getErrorArgs() {
        return errorArgs != null ? errorArgs.clone() : null;
    }
    
    @Override
    public String toString() {
        return String.format("FraudDetectionException{errorCode='%s', message='%s'}", 
                           errorCode, getMessage());
    }
}