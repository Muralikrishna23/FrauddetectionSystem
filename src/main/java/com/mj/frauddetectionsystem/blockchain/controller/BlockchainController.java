package com.mj.frauddetectionsystem.blockchain.controller;

import com.mj.frauddetectionsystem.blockchain.model.FraudBlock;
import com.mj.frauddetectionsystem.blockchain.service.BlockchainService;
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
@RequestMapping("/blockchain")
@Tag(name = "Blockchain", description = "Blockchain audit trail endpoints")
public class BlockchainController {
    private static final Logger logger = LoggerFactory.getLogger(BlockchainController.class);
    
    @Autowired
    private BlockchainService blockchainService;
    
    @PostMapping("/initialize")
    @Operation(summary = "Initialize blockchain")
    public ResponseEntity<Map<String, Object>> initializeBlockchain() {
        logger.info("Initializing blockchain");
        FraudBlock genesisBlock = blockchainService.initializeBlockchain();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Blockchain initialized successfully");
        response.put("genesisBlock", genesisBlock);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/validate")
    @Operation(summary = "Validate blockchain")
    public ResponseEntity<Map<String, Object>> validateBlockchain() {
        logger.info("Validating blockchain integrity");
        boolean isValid = blockchainService.validateBlockchain();
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        response.put("message", isValid ? "Blockchain is valid" : "Blockchain integrity compromised!");
        if (!isValid) logger.error("CRITICAL: Blockchain integrity check failed!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get transaction history")
    public ResponseEntity<List<FraudBlock>> getTransactionHistory(@PathVariable String transactionId) {
        logger.debug("Retrieving blockchain history for transaction: {}", transactionId);
        List<FraudBlock> history = blockchainService.getTransactionHistory(transactionId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/stats")
    @Operation(summary = "Get blockchain statistics")
    public ResponseEntity<Map<String, Object>> getBlockchainStats() {
        logger.debug("Retrieving blockchain statistics");
        Map<String, Object> stats = blockchainService.getBlockchainStats();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/high-risk")
    @Operation(summary = "Get high-risk blocks")
    public ResponseEntity<List<FraudBlock>> getHighRiskBlocks(
            @RequestParam(defaultValue = "0.7") BigDecimal minRiskScore) {
        logger.debug("Retrieving high-risk blocks with score >= {}", minRiskScore);
        List<FraudBlock> blocks = blockchainService.getHighRiskBlocks(minRiskScore);
        return ResponseEntity.ok(blocks);
    }
}