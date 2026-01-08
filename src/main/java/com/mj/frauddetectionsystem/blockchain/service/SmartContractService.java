package com.mj.frauddetectionsystem.blockchain.service;

import com.mj.frauddetectionsystem.blockchain.model.SmartContract;
import com.mj.frauddetectionsystem.blockchain.repository.SmartContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

@Service
public class SmartContractService {
    private static final Logger logger = LoggerFactory.getLogger(SmartContractService.class);
    
    @Autowired
    private SmartContractRepository smartContractRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Transactional
    public SmartContract createContract(String contractName, Map<String, Object> rules,
                                       String actionType, BigDecimal riskThreshold, String description) {
        try {
            String contractId = "SC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String rulesJson = objectMapper.writeValueAsString(rules);
            SmartContract contract = new SmartContract(contractId, contractName, rulesJson,
                actionType, riskThreshold, description);
            SmartContract saved = smartContractRepository.save(contract);
            logger.info("Created smart contract: {} with action: {}", contractId, actionType);
            return saved;
        } catch (Exception e) {
            logger.error("Failed to create smart contract: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create smart contract", e);
        }
    }
    
    @Transactional
    public List<Map<String, Object>> executeContracts(BigDecimal riskScore, Map<String, Object> transactionData) {
        List<SmartContract> applicableContracts = smartContractRepository.findApplicableContracts(riskScore);
        List<Map<String, Object>> executionResults = new ArrayList<>();
        logger.debug("Found {} applicable contracts for risk score {}", applicableContracts.size(), riskScore);
        for (SmartContract contract : applicableContracts) {
            Map<String, Object> result = new HashMap<>();
            result.put("contractId", contract.getContractId());
            result.put("contractName", contract.getContractName());
            result.put("actionType", contract.getActionType());
            result.put("executed", true);
            result.put("timestamp", LocalDateTime.now());
            String action = executeContractAction(contract, transactionData);
            result.put("actionTaken", action);
            contract.setLastExecutedAt(LocalDateTime.now());
            contract.setExecutionCount(contract.getExecutionCount() + 1);
            smartContractRepository.save(contract);
            logger.info("Executed contract {} - Action: {}", contract.getContractId(), action);
            executionResults.add(result);
        }
        return executionResults;
    }
    
    private String executeContractAction(SmartContract contract, Map<String, Object> transactionData) {
        String actionType = contract.getActionType();
        String userId = (String) transactionData.get("userId");
        String transactionId = (String) transactionData.get("transactionId");
        switch (actionType) {
            case "BLOCK_ACCOUNT":
                logger.warn("SMART CONTRACT ACTION: Blocking account {}", userId);
                return "Account " + userId + " has been temporarily blocked for review";
            case "FREEZE_FUNDS":
                logger.warn("SMART CONTRACT ACTION: Freezing funds for transaction {}", transactionId);
                return "Transaction " + transactionId + " funds frozen pending investigation";
            case "ALERT_ADMIN":
                logger.warn("SMART CONTRACT ACTION: Alerting admin for transaction {}", transactionId);
                return "Alert sent to fraud investigation team for transaction " + transactionId;
            case "REQUIRE_2FA":
                logger.info("SMART CONTRACT ACTION: Requiring 2FA for user {}", userId);
                return "Two-factor authentication required for user " + userId;
            case "DECLINE_TRANSACTION":
                logger.warn("SMART CONTRACT ACTION: Declining transaction {}", transactionId);
                return "Transaction " + transactionId + " automatically declined";
            default:
                return "Unknown action type: " + actionType;
        }
    }
    
    public List<SmartContract> getActiveContracts() {
        return smartContractRepository.findByIsActiveTrue();
    }
    
    public Optional<SmartContract> getContract(String contractId) {
        return smartContractRepository.findByContractId(contractId);
    }
    
    @Transactional
    public void deactivateContract(String contractId) {
        SmartContract contract = smartContractRepository.findByContractId(contractId)
            .orElseThrow(() -> new RuntimeException("Contract not found: " + contractId));
        contract.setIsActive(false);
        smartContractRepository.save(contract);
        logger.info("Deactivated smart contract: {}", contractId);
    }
    
    public Map<String, Object> getContractStats() {
        Map<String, Object> stats = new HashMap<>();
        long totalContracts = smartContractRepository.count();
        long activeContracts = smartContractRepository.findByIsActiveTrue().size();
        stats.put("totalContracts", totalContracts);
        stats.put("activeContracts", activeContracts);
        stats.put("inactiveContracts", totalContracts - activeContracts);
        return stats;
    }
}