package com.mj.frauddetectionsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "Response object containing fraud analysis results")
public class FraudDetectionResponse {
    
    @Schema(description = "Transaction identifier", example = "TXN001")
    private String transactionId;
    
    @Schema(description = "Whether transaction is flagged as fraudulent", example = "true")
    private boolean isFraudulent;
    
    @Schema(description = "Fraud confidence score (0.0-1.0)", example = "0.85")
    private BigDecimal confidenceScore;
    
    @Schema(description = "List of triggered fraud rules")
    private List<String> triggeredRules;
    
    @Schema(description = "Transaction status", example = "FLAGGED")
    private String status;
    
    @Schema(description = "Analysis processing time")
    private LocalDateTime processedAt;
    
    @Schema(description = "Processing time in milliseconds", example = "45")
    private Long processingTimeMs;
    
    // ⭐ NEW: Blockchain fields
    @Schema(description = "Blockchain block hash", example = "0000a1b2c3d4e5f6...")
    private String blockHash;
    
    @Schema(description = "Blockchain block index", example = "42")
    private Long blockIndex;
    
    @Schema(description = "Whether transaction was added to blockchain", example = "true")
    private boolean addedToBlockchain;
    
    @Schema(description = "Smart contract actions executed")
    private List<Map<String, Object>> smartContractActions;

    // Constructors
    public FraudDetectionResponse() {}

    public FraudDetectionResponse(String transactionId, boolean isFraudulent, 
                                BigDecimal confidenceScore, List<String> triggeredRules) {
        this.transactionId = transactionId;
        this.isFraudulent = isFraudulent;
        this.confidenceScore = confidenceScore;
        this.triggeredRules = triggeredRules;
        this.status = isFraudulent ? "FLAGGED" : "APPROVED";
        this.processedAt = LocalDateTime.now();
        this.addedToBlockchain = false; // Default
    }

    // Existing getters and setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public boolean isFraudulent() { return isFraudulent; }
    public void setFraudulent(boolean fraudulent) { isFraudulent = fraudulent; }

    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }

    public List<String> getTriggeredRules() { return triggeredRules; }
    public void setTriggeredRules(List<String> triggeredRules) { this.triggeredRules = triggeredRules; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public Long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    
    // ⭐ NEW: Blockchain getters and setters
    public String getBlockHash() { return blockHash; }
    public void setBlockHash(String blockHash) { this.blockHash = blockHash; }
    
    public Long getBlockIndex() { return blockIndex; }
    public void setBlockIndex(Long blockIndex) { this.blockIndex = blockIndex; }
    
    public boolean isAddedToBlockchain() { return addedToBlockchain; }
    public void setAddedToBlockchain(boolean addedToBlockchain) { 
        this.addedToBlockchain = addedToBlockchain; 
    }
    
    public List<Map<String, Object>> getSmartContractActions() { 
        return smartContractActions; 
    }
    public void setSmartContractActions(List<Map<String, Object>> smartContractActions) { 
        this.smartContractActions = smartContractActions; 
    }
}