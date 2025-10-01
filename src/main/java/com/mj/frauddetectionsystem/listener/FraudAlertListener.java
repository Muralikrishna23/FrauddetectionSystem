package com.mj.frauddetectionsystem.listener;

import com.mj.frauddetectionsystem.dto.FraudAlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FraudAlertListener {

    private static final Logger logger = LoggerFactory.getLogger(FraudAlertListener.class);

    @KafkaListener(topics = "fraud-alerts", groupId = "fraud-detection-group")
    public void handleFraudAlert(FraudAlertMessage alert) {
        logger.warn("🚨 FRAUD ALERT: Transaction {} for user {} flagged with confidence {}", 
                   alert.getTransactionId(), alert.getUserId(), alert.getConfidenceScore());
        
        // Example: Block high-confidence fraud
        if (alert.getConfidenceScore().compareTo(new BigDecimal("0.8")) >= 0) {
            logger.error("🛑 HIGH CONFIDENCE FRAUD - Transaction {} blocked", alert.getTransactionId());
            // Implement blocking logic here
        }
        
        // Here you could implement:
        // - Send email notifications
        // - SMS alerts
        // - Push notifications to mobile apps
        // - Integration with external fraud management systems
        // - Automatic transaction blocking
    }
}