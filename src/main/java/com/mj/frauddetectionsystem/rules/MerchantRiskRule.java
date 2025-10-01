package com.mj.frauddetectionsystem.rules;


import com.mj.frauddetectionsystem.model.Transaction;
import com.mj.frauddetectionsystem.model.MerchantCategory;
import com.mj.frauddetectionsystem.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MerchantRiskRule implements FraudDetectionRule {
    
    private static final Logger logger = LoggerFactory.getLogger(MerchantRiskRule.class);
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Value("${fraud.detection.rules.merchant.high-risk-threshold:500.0}")
    private BigDecimal highRiskThreshold;
    
    @Value("${fraud.detection.rules.merchant.critical-risk-threshold:1000.0}")
    private BigDecimal criticalRiskThreshold;
    
    @Value("${fraud.detection.rules.merchant.enable-merchant-velocity:true}")
    private boolean enableMerchantVelocityCheck;
    
    // High-risk merchant patterns
    private static final Set<String> HIGH_RISK_MERCHANTS = Set.of(
        "CRYPTO", "GAMBLING", "ADULT", "PREPAID", "WIRE_TRANSFER",
        "MONEY_ORDER", "PAWN_SHOP", "CHECK_CASHING", "LOTTERY"
    );
    
    // Critical risk merchant patterns
    private static final Set<String> CRITICAL_RISK_MERCHANTS = Set.of(
        "OFFSHORE_GAMBLING", "ILLEGAL_DRUGS", "WEAPONS", "STOLEN_GOODS",
        "MONEY_LAUNDERING", "TERRORIST_FINANCING", "PYRAMID_SCHEME"
    );
    
    // Suspicious merchant name patterns
    private static final Set<String> SUSPICIOUS_MERCHANT_PATTERNS = Set.of(
        "TEMP", "TEST", "UNKNOWN", "CASH_ADVANCE", "PAYDAY_LOAN",
        "QUICK_LOAN", "INSTANT_MONEY", "EASY_CASH", "FAST_MONEY"
    );
    
    // Risk multipliers by category
    private static final Map<MerchantCategory.RiskLevel, BigDecimal> RISK_MULTIPLIERS = Map.of(
        MerchantCategory.RiskLevel.LOW, new BigDecimal("0.1"),
        MerchantCategory.RiskLevel.MEDIUM, new BigDecimal("0.4"),
        MerchantCategory.RiskLevel.HIGH, new BigDecimal("0.7"),
        MerchantCategory.RiskLevel.CRITICAL, new BigDecimal("0.95")
    );

    @Override
    public boolean isFraudulent(Transaction transaction) {
        try {
            logger.debug("Evaluating merchant risk for transaction: {}", transaction.getTransactionId());
            
            MerchantCategory category = transaction.getMerchantCategory();
            if (category == null) {
                logger.warn("No merchant category found for transaction: {}", transaction.getTransactionId());
                return false;
            }
            
            // Check category risk level
            if (isCategoryHighRisk(category)) {
                logger.info("Transaction {} flagged for high-risk category: {}", 
                           transaction.getTransactionId(), category.getCategoryName());
                return true;
            }
            
            // Check merchant name patterns
            if (hasSuspiciousMerchantName(transaction)) {
                logger.info("Transaction {} flagged for suspicious merchant name: {}", 
                           transaction.getTransactionId(), transaction.getMerchantName());
                return true;
            }
            
            // Check amount thresholds for risky categories
            if (exceedsRiskThreshold(transaction, category)) {
                logger.info("Transaction {} flagged for exceeding risk threshold: ${} in category: {}", 
                           transaction.getTransactionId(), transaction.getAmount(), category.getCategoryName());
                return true;
            }
            
            // Check merchant velocity (optional)
            if (enableMerchantVelocityCheck && hasHighMerchantVelocity(transaction)) {
                logger.info("Transaction {} flagged for high merchant velocity", transaction.getTransactionId());
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("Error evaluating merchant risk for transaction {}: {}", 
                        transaction.getTransactionId(), e.getMessage(), e);
            return false; // Don't flag due to technical errors
        }
    }

    @Override
    public String getRuleName() {
        return "Enhanced Merchant Risk Rule";
    }
    
    @Override
    public BigDecimal calculateConfidence(Transaction transaction) {
        try {
            MerchantCategory category = transaction.getMerchantCategory();
            if (category == null) {
                return BigDecimal.ZERO;
            }
            
            BigDecimal baseConfidence = RISK_MULTIPLIERS.getOrDefault(
                category.getRiskLevel(), BigDecimal.ZERO);
            
            // Adjust confidence based on various factors
            BigDecimal adjustedConfidence = baseConfidence;
            
            // Factor 1: Merchant name suspicion
            if (hasSuspiciousMerchantName(transaction)) {
                adjustedConfidence = adjustedConfidence.add(new BigDecimal("0.2"));
            }
            
            // Factor 2: Amount relative to category risk
            if (category.getRiskLevel() == MerchantCategory.RiskLevel.HIGH && 
                transaction.getAmount().compareTo(highRiskThreshold) > 0) {
                adjustedConfidence = adjustedConfidence.add(new BigDecimal("0.15"));
            }
            
            if (category.getRiskLevel() == MerchantCategory.RiskLevel.CRITICAL && 
                transaction.getAmount().compareTo(criticalRiskThreshold) > 0) {
                adjustedConfidence = adjustedConfidence.add(new BigDecimal("0.25"));
            }
            
            // Factor 3: Time-based risk (late night high-risk transactions)
            int hour = transaction.getTimestamp().getHour();
            if ((hour >= 23 || hour <= 5) && category.getRiskLevel().ordinal() >= 2) {
                adjustedConfidence = adjustedConfidence.add(new BigDecimal("0.1"));
            }
            
            // Factor 4: Merchant velocity
            if (enableMerchantVelocityCheck && hasHighMerchantVelocity(transaction)) {
                adjustedConfidence = adjustedConfidence.add(new BigDecimal("0.3"));
            }
            
            // Cap at 1.0
            return adjustedConfidence.min(new BigDecimal("1.0"));
            
        } catch (Exception e) {
            logger.error("Error calculating confidence for transaction {}: {}", 
                        transaction.getTransactionId(), e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Check if the merchant category is high risk
     */
    private boolean isCategoryHighRisk(MerchantCategory category) {
        return category.getRiskLevel() == MerchantCategory.RiskLevel.HIGH ||
               category.getRiskLevel() == MerchantCategory.RiskLevel.CRITICAL;
    }
    
    /**
     * Check if merchant name contains suspicious patterns
     */
    private boolean hasSuspiciousMerchantName(Transaction transaction) {
        String merchantName = transaction.getMerchantName();
        if (merchantName == null || merchantName.trim().isEmpty()) {
            return true; // No merchant name is suspicious
        }
        
        String upperMerchantName = merchantName.toUpperCase();
        
        // Check for suspicious patterns
        for (String pattern : SUSPICIOUS_MERCHANT_PATTERNS) {
            if (upperMerchantName.contains(pattern)) {
                return true;
            }
        }
        
        // Check for high-risk merchant types
        for (String riskType : HIGH_RISK_MERCHANTS) {
            if (upperMerchantName.contains(riskType)) {
                return true;
            }
        }
        
        // Check for critical risk merchant types
        for (String criticalType : CRITICAL_RISK_MERCHANTS) {
            if (upperMerchantName.contains(criticalType)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if transaction amount exceeds risk thresholds for the category
     */
    private boolean exceedsRiskThreshold(Transaction transaction, MerchantCategory category) {
        BigDecimal amount = transaction.getAmount();
        
        switch (category.getRiskLevel()) {
            case HIGH:
                return amount.compareTo(highRiskThreshold) > 0;
            case CRITICAL:
                return amount.compareTo(criticalRiskThreshold) > 0;
            case MEDIUM:
                // For medium risk, use a higher threshold
                return amount.compareTo(highRiskThreshold.multiply(new BigDecimal("2"))) > 0;
            default:
                return false;
        }
    }
    
    /**
     * Check for high transaction velocity at the same merchant
     */
    private boolean hasHighMerchantVelocity(Transaction transaction) {
        try {
            if (transaction.getMerchantName() == null) {
                return false;
            }
            
            LocalDateTime oneHourAgo = transaction.getTimestamp().minusHours(1);
            
            // Find recent transactions from the same user at the same merchant
            List<Transaction> recentTransactions = transactionRepository.findUserTransactionsAfter(
                transaction.getUser(), oneHourAgo);
            
            long samemerchantCount = recentTransactions.stream()
                .filter(t -> transaction.getMerchantName().equals(t.getMerchantName()))
                .count();
            
            // Flag if more than 3 transactions at same merchant in 1 hour
            return samemerchantCount >= 3;
            
        } catch (Exception e) {
            logger.error("Error checking merchant velocity: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get detailed risk analysis for reporting
     */
    public MerchantRiskAnalysis analyzeRisk(Transaction transaction) {
        MerchantRiskAnalysis analysis = new MerchantRiskAnalysis();
        analysis.setTransactionId(transaction.getTransactionId());
        analysis.setMerchantName(transaction.getMerchantName());
        
        MerchantCategory category = transaction.getMerchantCategory();
        if (category != null) {
            analysis.setCategoryName(category.getCategoryName());
            analysis.setCategoryRiskLevel(category.getRiskLevel().toString());
            analysis.setCategoryRisk(isCategoryHighRisk(category));
        }
        
        analysis.setSuspiciousMerchantName(hasSuspiciousMerchantName(transaction));
        analysis.setExceedsThreshold(exceedsRiskThreshold(transaction, category));
        analysis.setHighVelocity(enableMerchantVelocityCheck && hasHighMerchantVelocity(transaction));
        analysis.setConfidenceScore(calculateConfidence(transaction));
        analysis.setOverallRisk(isFraudulent(transaction));
        
        return analysis;
    }
    
    /**
     * Risk analysis result class
     */
    public static class MerchantRiskAnalysis {
        private String transactionId;
        private String merchantName;
        private String categoryName;
        private String categoryRiskLevel;
        private boolean categoryRisk;
        private boolean suspiciousMerchantName;
        private boolean exceedsThreshold;
        private boolean highVelocity;
        private BigDecimal confidenceScore;
        private boolean overallRisk;
        
        // Getters and setters
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        
        public String getMerchantName() { return merchantName; }
        public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
        
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        
        public String getCategoryRiskLevel() { return categoryRiskLevel; }
        public void setCategoryRiskLevel(String categoryRiskLevel) { this.categoryRiskLevel = categoryRiskLevel; }
        
        public boolean isCategoryRisk() { return categoryRisk; }
        public void setCategoryRisk(boolean categoryRisk) { this.categoryRisk = categoryRisk; }
        
        public boolean isSuspiciousMerchantName() { return suspiciousMerchantName; }
        public void setSuspiciousMerchantName(boolean suspiciousMerchantName) { this.suspiciousMerchantName = suspiciousMerchantName; }
        
        public boolean isExceedsThreshold() { return exceedsThreshold; }
        public void setExceedsThreshold(boolean exceedsThreshold) { this.exceedsThreshold = exceedsThreshold; }
        
        public boolean isHighVelocity() { return highVelocity; }
        public void setHighVelocity(boolean highVelocity) { this.highVelocity = highVelocity; }
        
        public BigDecimal getConfidenceScore() { return confidenceScore; }
        public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
        
        public boolean isOverallRisk() { return overallRisk; }
        public void setOverallRisk(boolean overallRisk) { this.overallRisk = overallRisk; }
        
        @Override
        public String toString() {
            return String.format("MerchantRiskAnalysis{txnId='%s', merchant='%s', category='%s', risk=%s, confidence=%.2f}",
                    transactionId, merchantName, categoryName, overallRisk, confidenceScore);
        }
    }
}