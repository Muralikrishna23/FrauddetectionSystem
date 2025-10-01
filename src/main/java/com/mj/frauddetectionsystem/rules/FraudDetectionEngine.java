package com.mj.frauddetectionsystem.rules;


import com.mj.frauddetectionsystem.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FraudDetectionEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(FraudDetectionEngine.class);
    
    private final List<FraudDetectionRule> rules = new ArrayList<>();
    private final Map<String, Integer> ruleViolationCounts = new HashMap<>();

    public void addRule(FraudDetectionRule rule) {
        rules.add(rule);
        ruleViolationCounts.put(rule.getRuleName(), 0);
        logger.info("Added fraud detection rule: {}", rule.getRuleName());
    }

    public FraudDetectionResult analyzeTransaction(Transaction transaction) {
        logger.debug("Analyzing transaction: {}", transaction.getTransactionId());
        
        List<String> triggeredRules = new ArrayList<>();
        boolean isFraudulent = false;
        BigDecimal totalConfidence = BigDecimal.ZERO;
        int ruleCount = 0;

        for (FraudDetectionRule rule : rules) {
            try {
                if (rule.isFraudulent(transaction)) {
                    String ruleName = rule.getRuleName();
                    triggeredRules.add(ruleName);
                    ruleViolationCounts.put(ruleName, ruleViolationCounts.get(ruleName) + 1);
                    
                    BigDecimal confidence = rule.calculateConfidence(transaction);
                    totalConfidence = totalConfidence.add(confidence);
                    ruleCount++;
                    
                    isFraudulent = true;
                    
                    logger.debug("Rule triggered: {} with confidence: {}", ruleName, confidence);
                }
            } catch (Exception e) {
                logger.error("Error executing rule {}: {}", rule.getRuleName(), e.getMessage(), e);
            }
        }

        // Calculate average confidence
        BigDecimal averageConfidence = ruleCount > 0 ? 
            totalConfidence.divide(new BigDecimal(ruleCount), 2, BigDecimal.ROUND_HALF_UP) : 
            BigDecimal.ZERO;

        // Update transaction
        transaction.setFraudulent(isFraudulent);
        transaction.setFraudScore(averageConfidence);
        
        if (isFraudulent) {
            transaction.setProcessingStatus(Transaction.ProcessingStatus.UNDER_REVIEW);
            logger.warn("Transaction {} flagged as fraudulent with confidence {}", 
                       transaction.getTransactionId(), averageConfidence);
        } else {
            transaction.setProcessingStatus(Transaction.ProcessingStatus.APPROVED);
            logger.debug("Transaction {} approved", transaction.getTransactionId());
        }

        return new FraudDetectionResult(transaction, isFraudulent, triggeredRules, averageConfidence);
    }

    public Map<String, Integer> getRuleViolationCounts() {
        return new HashMap<>(ruleViolationCounts);
    }

    public void generateReport() {
        logger.info("=== FRAUD DETECTION ENGINE REPORT ===");
        logger.info("Active rules: {}", rules.size());
        logger.info("Rule violation summary:");
        ruleViolationCounts.forEach((rule, count) -> 
            logger.info("- {}: {} violations", rule, count));
    }
}
