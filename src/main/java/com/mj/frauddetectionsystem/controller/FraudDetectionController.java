package com.mj.frauddetectionsystem.controller;


import com.mj.frauddetectionsystem.dto.*;
import com.mj.frauddetectionsystem.model.*;
import com.mj.frauddetectionsystem.service.FraudDetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fraud-detection")
@Tag(name = "Fraud Detection", description = "Core fraud detection and analysis endpoints")
@SecurityRequirement(name = "basicAuth")
public class FraudDetectionController {

    private static final Logger logger = LoggerFactory.getLogger(FraudDetectionController.class);

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @PostMapping("/analyze")
    @Operation(summary = "Analyze transaction for fraud", 
               description = "Process a single transaction and return fraud analysis results with confidence score")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction analyzed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "404", description = "User or merchant category not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<FraudDetectionResponse> analyzeTransaction(
            @Valid @RequestBody TransactionRequest request) {
        
        logger.info("Received fraud analysis request for transaction: {}", request.getTransactionId());
        
        FraudDetectionResponse response = fraudDetectionService.processTransaction(request);
        
        if (response.isFraudulent()) {
            logger.warn("FRAUD DETECTED - Transaction: {}, Confidence: {}", 
                       request.getTransactionId(), response.getConfidenceScore());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch-analyze")
    @Operation(summary = "Batch analyze transactions", 
               description = "Analyze multiple transactions in a single request for bulk processing")
    public ResponseEntity<List<FraudDetectionResponse>> batchAnalyze(
            @Valid @RequestBody List<TransactionRequest> requests) {
        
        logger.info("Received batch fraud analysis request for {} transactions", requests.size());
        
        List<FraudDetectionResponse> responses = requests.stream()
                .map(fraudDetectionService::processTransaction)
                .toList();
        
        long fraudCount = responses.stream().mapToLong(r -> r.isFraudulent() ? 1 : 0).sum();
        logger.info("Batch analysis completed - {} fraudulent out of {} transactions", 
                   fraudCount, requests.size());
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/transactions/{userId}")
    @Operation(summary = "Get user transactions")
    public ResponseEntity<List<Transaction>> getUserTransactions(
            @Parameter(description = "User ID", example = "user001") 
            @PathVariable String userId) {
        
        logger.debug("Retrieving transactions for user: {}", userId);
        List<Transaction> transactions = fraudDetectionService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/alerts/{userId}")
    @Operation(summary = "Get user fraud alerts")
    public ResponseEntity<List<FraudAlert>> getUserAlerts(
            @Parameter(description = "User ID", example = "user001") 
            @PathVariable String userId) {
        
        logger.debug("Retrieving fraud alerts for user: {}", userId);
        List<FraudAlert> alerts = fraudDetectionService.getUserAlerts(userId);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/alerts/open")
    @Operation(summary = "Get open fraud alerts")
    public ResponseEntity<List<FraudAlert>> getOpenAlerts() {
        logger.debug("Retrieving all open fraud alerts");
        List<FraudAlert> alerts = fraudDetectionService.getOpenAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get fraud statistics")
    public ResponseEntity<FraudStatistics> getStatistics() {
        logger.debug("Retrieving fraud detection statistics");
        FraudStatistics stats = fraudDetectionService.getFraudStatistics();
        return ResponseEntity.ok(stats);
    }
}
