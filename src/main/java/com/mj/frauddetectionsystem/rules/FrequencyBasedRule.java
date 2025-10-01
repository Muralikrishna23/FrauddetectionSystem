package com.mj.frauddetectionsystem.rules;


import com.mj.frauddetectionsystem.model.Transaction;
import com.mj.frauddetectionsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FrequencyBasedRule implements FraudDetectionRule {
    
    @Value("${fraud.detection.rules.frequency.max-transactions:5}")
    private int maxTransactions;
    
    @Value("${fraud.detection.rules.frequency.time-window-minutes:30}")
    private int timeWindowMinutes;
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean isFraudulent(Transaction transaction) {
        LocalDateTime cutoffTime = transaction.getTimestamp().minusMinutes(timeWindowMinutes);
        List<Transaction> recentTransactions = transactionRepository.findUserTransactionsAfter(
            transaction.getUser(), cutoffTime);
        
        return recentTransactions.size() >= maxTransactions;
    }

    @Override
    public String getRuleName() {
        return "Frequency-Based Rule";
    }
    
    @Override
    public BigDecimal calculateConfidence(Transaction transaction) {
        LocalDateTime cutoffTime = transaction.getTimestamp().minusMinutes(timeWindowMinutes);
        List<Transaction> recentTransactions = transactionRepository.findUserTransactionsAfter(
            transaction.getUser(), cutoffTime);
        
        if (recentTransactions.size() >= maxTransactions) {
            // Higher frequency = higher confidence
            double ratio = (double) recentTransactions.size() / maxTransactions;
            return new BigDecimal(Math.min(ratio * 0.6, 0.9));
        }
        
        return BigDecimal.ZERO;
    }
}

