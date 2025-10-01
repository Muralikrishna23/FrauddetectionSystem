package com.mj.frauddetectionsystem.rules;

import com.mj.frauddetectionsystem.model.Transaction;
import java.math.BigDecimal;
import java.util.List;

public class FraudDetectionResult {
    private final Transaction transaction;
    private final boolean isFraudulent;
    private final List<String> triggeredRules;
    private final BigDecimal confidenceScore;
    private final long timestamp;

    public FraudDetectionResult(Transaction transaction, boolean isFraudulent, 
                              List<String> triggeredRules, BigDecimal confidenceScore) {
        this.transaction = transaction;
        this.isFraudulent = isFraudulent;
        this.triggeredRules = triggeredRules;
        this.confidenceScore = confidenceScore;
        this.timestamp = System.currentTimeMillis();
    }

    public Transaction getTransaction() { return transaction; }
    public boolean isFraudulent() { return isFraudulent; }
    public List<String> getTriggeredRules() { return triggeredRules; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("FraudDetectionResult{txnId='%s', fraudulent=%s, confidence=%.2f, rules=%s}", 
                transaction.getTransactionId(), isFraudulent, confidenceScore, triggeredRules);
    }
}