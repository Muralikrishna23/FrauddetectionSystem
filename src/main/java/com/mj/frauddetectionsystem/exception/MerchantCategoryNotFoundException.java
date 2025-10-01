package com.mj.frauddetectionsystem.exception;

public class MerchantCategoryNotFoundException extends FraudDetectionException {
    
    private String categoryCode;

    public MerchantCategoryNotFoundException(String categoryCode) {
        super("MERCHANT_CATEGORY_NOT_FOUND", String.format("Merchant category not found with code: %s", categoryCode));
        this.categoryCode = categoryCode;
    }

    public MerchantCategoryNotFoundException(String categoryCode, String message) {
        super("MERCHANT_CATEGORY_NOT_FOUND", message);
        this.categoryCode = categoryCode;
    }

    public MerchantCategoryNotFoundException(String categoryCode, String message, Throwable cause) {
        super("MERCHANT_CATEGORY_NOT_FOUND", message, cause);
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }
}
