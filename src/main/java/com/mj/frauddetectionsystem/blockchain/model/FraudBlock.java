package com.mj.frauddetectionsystem.blockchain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;

@Entity
@Table(name = "fraud_blocks")
public class FraudBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "block_index", nullable = false)
    private Long blockIndex;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "transaction_data", columnDefinition = "TEXT", nullable = false)
    private String transactionData;
    @Column(name = "previous_hash", nullable = false)
    private String previousHash;
    @Column(name = "current_hash", nullable = false)
    private String currentHash;
    @Column(nullable = false)
    private Long nonce;
    @Column(name = "fraud_decision", nullable = false)
    private String fraudDecision;
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;
    @Column(name = "risk_score", precision = 5, scale = 2, nullable = false)
    private BigDecimal riskScore;
    @Column(name = "fraud_reasons", columnDefinition = "TEXT")
    private String fraudReasons;
    @Column(name = "validator_signature", nullable = false)
    private String validatorSignature;
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;

    public FraudBlock() {
        this.timestamp = LocalDateTime.now();
        this.isValid = true;
        this.nonce = 0L;
    }

    public FraudBlock(Long blockIndex, String transactionData, String previousHash,
                     String transactionId, String fraudDecision, BigDecimal riskScore, String fraudReasons) {
        this();
        this.blockIndex = blockIndex;
        this.transactionData = transactionData;
        this.previousHash = previousHash;
        this.transactionId = transactionId;
        this.fraudDecision = fraudDecision;
        this.riskScore = riskScore;
        this.fraudReasons = fraudReasons;
    }

    public String calculateHash() {
        try {
            String data = blockIndex + timestamp.toString() + transactionData + 
                         previousHash + nonce + fraudDecision + transactionId + riskScore;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!currentHash.substring(0, difficulty).equals(target)) {
            nonce++;
            currentHash = calculateHash();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBlockIndex() { return blockIndex; }
    public void setBlockIndex(Long blockIndex) { this.blockIndex = blockIndex; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getTransactionData() { return transactionData; }
    public void setTransactionData(String transactionData) { this.transactionData = transactionData; }
    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }
    public String getCurrentHash() { return currentHash; }
    public void setCurrentHash(String currentHash) { this.currentHash = currentHash; }
    public Long getNonce() { return nonce; }
    public void setNonce(Long nonce) { this.nonce = nonce; }
    public String getFraudDecision() { return fraudDecision; }
    public void setFraudDecision(String fraudDecision) { this.fraudDecision = fraudDecision; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public String getFraudReasons() { return fraudReasons; }
    public void setFraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; }
    public String getValidatorSignature() { return validatorSignature; }
    public void setValidatorSignature(String validatorSignature) { this.validatorSignature = validatorSignature; }
    public Boolean getIsValid() { return isValid; }
    public void setIsValid(Boolean isValid) { this.isValid = isValid; }
}