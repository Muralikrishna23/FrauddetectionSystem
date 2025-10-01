package com.mj.frauddetectionsystem.dto;


import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request object for transaction fraud analysis")
public class TransactionRequest {
    
    @NotBlank(message = "Transaction ID is required")
    @Schema(description = "Unique transaction identifier", example = "TXN001")
    private String transactionId;
    
    @NotBlank(message = "User ID is required")
    @Schema(description = "User identifier", example = "user001")
    private String userId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Schema(description = "Transaction amount", example = "1500.00")
    private BigDecimal amount;
    
    @NotBlank(message = "Merchant category is required")
    @Schema(description = "Merchant category code", example = "ELE")
    private String merchantCategoryCode;
    
    @Schema(description = "Merchant name", example = "Best Buy")
    private String merchantName;
    
    @NotBlank(message = "Location is required")
    @Schema(description = "Transaction location", example = "New York, USA")
    private String location;
    
    @NotNull(message = "Payment method is required")
    @Pattern(regexp = "CREDIT_CARD|DEBIT_CARD|BANK_TRANSFER|DIGITAL_WALLET|CASH|CHECK", 
             message = "Invalid payment method")
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;
    
    @Schema(description = "Transaction description", example = "Electronics purchase")
    private String description;

    // Constructors
    public TransactionRequest() {}

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMerchantCategoryCode() { return merchantCategoryCode; }
    public void setMerchantCategoryCode(String merchantCategoryCode) { this.merchantCategoryCode = merchantCategoryCode; }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("TransactionRequest{txnId='%s', userId='%s', amount=%.2f, category='%s'}", 
                transactionId, userId, amount, merchantCategoryCode);
    }
}

