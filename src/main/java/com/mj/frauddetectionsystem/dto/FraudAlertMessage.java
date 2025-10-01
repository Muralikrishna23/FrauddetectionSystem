package com.mj.frauddetectionsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FraudAlertMessage {
    private String transactionId;
    private String userId;
    private BigDecimal amount;
    private BigDecimal confidenceScore;
    private List<String> triggeredRules;
    private LocalDateTime alertTime;

    public FraudAlertMessage() {
        this.alertTime = LocalDateTime.now();
    }

    public FraudAlertMessage(String transactionId, String userId, BigDecimal amount, 
                           BigDecimal confidenceScore, List<String> triggeredRules) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.confidenceScore = confidenceScore;
        this.triggeredRules = triggeredRules;
        this.alertTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }

    public List<String> getTriggeredRules() { return triggeredRules; }
    public void setTriggeredRules(List<String> triggeredRules) { this.triggeredRules = triggeredRules; }

    public LocalDateTime getAlertTime() { return alertTime; }
    public void setAlertTime(LocalDateTime alertTime) { this.alertTime = alertTime; }
}