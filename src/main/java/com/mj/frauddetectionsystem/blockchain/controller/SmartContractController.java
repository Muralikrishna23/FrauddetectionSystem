package com.mj.frauddetectionsystem.blockchain.controller;

import com.mj.frauddetectionsystem.blockchain.model.SmartContract;
import com.mj.frauddetectionsystem.blockchain.service.SmartContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/smart-contracts")
@Tag(name = "Smart Contracts", description = "Smart contract management endpoints")
public class SmartContractController {
    private static final Logger logger = LoggerFactory.getLogger(SmartContractController.class);
    
    @Autowired
    private SmartContractService smartContractService;
    
    @PostMapping("/create")
    @Operation(summary = "Create smart contract")
    public ResponseEntity<Map<String, Object>> createContract(@RequestBody Map<String, Object> request) {
        String contractName = (String) request.get("contractName");
        Map<String, Object> rules = (Map<String, Object>) request.get("rules");
        String actionType = (String) request.get("actionType");
        BigDecimal riskThreshold = new BigDecimal(request.get("riskThreshold").toString());
        String description = (String) request.get("description");
        logger.info("Creating smart contract: {}", contractName);
        SmartContract contract = smartContractService.createContract(
            contractName, rules, actionType, riskThreshold, description
        );
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Smart contract created successfully");
        response.put("contract", contract);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active contracts")
    public ResponseEntity<List<SmartContract>> getActiveContracts() {
        logger.debug("Retrieving active smart contracts");
        List<SmartContract> contracts = smartContractService.getActiveContracts();
        return ResponseEntity.ok(contracts);
    }
    
    @GetMapping("/{contractId}")
    @Operation(summary = "Get contract")
    public ResponseEntity<SmartContract> getContract(@PathVariable String contractId) {
        logger.debug("Retrieving smart contract: {}", contractId);
        SmartContract contract = smartContractService.getContract(contractId)
            .orElseThrow(() -> new RuntimeException("Contract not found"));
        return ResponseEntity.ok(contract);
    }
    
    @PostMapping("/{contractId}/deactivate")
    @Operation(summary = "Deactivate contract")
    public ResponseEntity<Map<String, Object>> deactivateContract(@PathVariable String contractId) {
        logger.info("Deactivating smart contract: {}", contractId);
        smartContractService.deactivateContract(contractId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Contract deactivated successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "Get contract statistics")
    public ResponseEntity<Map<String, Object>> getContractStats() {
        logger.debug("Retrieving smart contract statistics");
        Map<String, Object> stats = smartContractService.getContractStats();
        return ResponseEntity.ok(stats);
    }
}
