package com.mj.frauddetectionsystem.config;

import com.mj.frauddetectionsystem.blockchain.service.BlockchainService;
import com.mj.frauddetectionsystem.blockchain.service.SmartContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class BlockchainInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(BlockchainInitializer.class);
    
    @Autowired
    private BlockchainService blockchainService;
    
    @Autowired
    private SmartContractService smartContractService;
    
    @Override
    public void run(String... args) {
        logger.info("🔗 Initializing Blockchain System...");
        blockchainService.initializeBlockchain();
        logger.info("✅ Blockchain initialized successfully");
        createDefaultSmartContracts();
        logger.info("✅ Smart contracts initialized");
        boolean isValid = blockchainService.validateBlockchain();
        if (isValid) {
            logger.info("✅ Blockchain validation passed");
        } else {
            logger.error("❌ Blockchain validation failed!");
        }
    }
    
    private void createDefaultSmartContracts() {
        try {
            Map<String, Object> rules1 = new HashMap<>();
            rules1.put("condition", "riskScore >= 0.8");
            rules1.put("action", "BLOCK_ACCOUNT");
            smartContractService.createContract(
                "High Risk Blocker", rules1, "BLOCK_ACCOUNT",
                new BigDecimal("0.8"), "Automatically block accounts with risk score >= 0.8"
            );
            Map<String, Object> rules2 = new HashMap<>();
            rules2.put("condition", "riskScore >= 0.5 && riskScore < 0.8");
            rules2.put("action", "ALERT_ADMIN");
            smartContractService.createContract(
                "Medium Risk Alert", rules2, "ALERT_ADMIN",
                new BigDecimal("0.5"), "Send alert to admins for medium-risk transactions"
            );
            Map<String, Object> rules3 = new HashMap<>();
            rules3.put("condition", "riskScore >= 0.6");
            rules3.put("action", "REQUIRE_2FA");
            smartContractService.createContract(
                "2FA Requirement", rules3, "REQUIRE_2FA",
                new BigDecimal("0.6"), "Require two-factor authentication for suspicious transactions"
            );
            logger.info("Created {} default smart contracts", 3);
        } catch (Exception e) {
            logger.warn("Some smart contracts may already exist: {}", e.getMessage());
        }
    }
}