package com.mj.frauddetectionsystem.rules;

import com.mj.frauddetectionsystem.model.Transaction;
import java.math.BigDecimal;

public interface FraudDetectionRule {
    boolean isFraudulent(Transaction transaction);
    String getRuleName();
    BigDecimal calculateConfidence(Transaction transaction);
}
