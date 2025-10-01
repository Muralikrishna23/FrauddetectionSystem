package com.mj.frauddetectionsystem.rules;


import com.mj.frauddetectionsystem.model.Transaction;
import com.mj.frauddetectionsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountBasedRule implements FraudDetectionRule {
    
    @Value("${fraud.detection.rules.amount.threshold:10000.0}")
    private BigDecimal threshold;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean isFraudulent(Transaction transaction) {
        // Check if amount exceeds threshold
        if (transaction.getAmount().compareTo(threshold) > 0) {
            return true;
        }
        
        // Check if amount is significantly higher than user's average
        BigDecimal userAverage = transactionRepository.getAverageTransactionAmount(transaction.getUser());
        if (userAverage != null && userAverage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = transaction.getAmount().divide(userAverage, 2, BigDecimal.ROUND_HALF_UP);
            return ratio.compareTo(new BigDecimal("5.0")) > 0; // 5x their average
        }
        
        return false;
    }

    @Override
    public String getRuleName() {
        return "Amount-Based Rule";
    }
    
    @Override
    public BigDecimal calculateConfidence(Transaction transaction) {
        BigDecimal confidence = BigDecimal.ZERO;
        
        if (transaction.getAmount().compareTo(threshold) > 0) {
            // Higher amounts get higher confidence scores
            BigDecimal ratio = transaction.getAmount().divide(threshold, 2, BigDecimal.ROUND_HALF_UP);
            confidence = ratio.multiply(new BigDecimal("0.3")).min(new BigDecimal("0.8"));
        }
        
        return confidence;
    }
}