package com.mj.frauddetectionsystem.config;


import com.mj.frauddetectionsystem.rules.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableJpaAuditing
public class FraudDetectionConfig {

    @Autowired
    private FraudDetectionEngine fraudDetectionEngine;
    
    @Autowired
    private AmountBasedRule amountBasedRule;
    
    @Autowired
    private FrequencyBasedRule frequencyBasedRule;
    
    @Autowired
    private MerchantRiskRule merchantRiskRule;

    @PostConstruct
    public void configureRules() {
        // Add all fraud detection rules to the engine
        fraudDetectionEngine.addRule(amountBasedRule);
        fraudDetectionEngine.addRule(frequencyBasedRule);
        fraudDetectionEngine.addRule(merchantRiskRule);
    }
}
