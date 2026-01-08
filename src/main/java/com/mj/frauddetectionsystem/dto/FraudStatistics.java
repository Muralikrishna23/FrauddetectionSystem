package com.mj.frauddetectionsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class FraudStatistics {
    private long totalTransactions;
    private long fraudulentTransactions;
    private BigDecimal fraudRate;
    private long todayTransactions;
    private long todayFraudulent;
    private BigDecimal todayFraudRate;
    private LocalDateTime lastUpdated;
    
    //  Blockchain statistics
    private Map<String, Object> blockchainStats;

    // Existing constructor
    public FraudStatistics(long totalTransactions, long fraudulentTransactions, 
                         long todayTransactions, long todayFraudulent) {
        this.totalTransactions = totalTransactions;
        this.fraudulentTransactions = fraudulentTransactions;
        this.fraudRate = totalTransactions > 0 ? 
            new BigDecimal(fraudulentTransactions).divide(new BigDecimal(totalTransactions), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")) : BigDecimal.ZERO;
        
        this.todayTransactions = todayTransactions;
        this.todayFraudulent = todayFraudulent;
        this.todayFraudRate = todayTransactions > 0 ? 
            new BigDecimal(todayFraudulent).divide(new BigDecimal(todayTransactions), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100")) : BigDecimal.ZERO;
        
        this.lastUpdated = LocalDateTime.now();
    }

    // getters and setters
    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public long getFraudulentTransactions() { return fraudulentTransactions; }
    public void setFraudulentTransactions(long fraudulentTransactions) { this.fraudulentTransactions = fraudulentTransactions; }

    public BigDecimal getFraudRate() { return fraudRate; }
    public void setFraudRate(BigDecimal fraudRate) { this.fraudRate = fraudRate; }

    public long getTodayTransactions() { return todayTransactions; }
    public void setTodayTransactions(long todayTransactions) { this.todayTransactions = todayTransactions; }

    public long getTodayFraudulent() { return todayFraudulent; }
    public void setTodayFraudulent(long todayFraudulent) { this.todayFraudulent = todayFraudulent; }

    public BigDecimal getTodayFraudRate() { return todayFraudRate; }
    public void setTodayFraudRate(BigDecimal todayFraudRate) { this.todayFraudRate = todayFraudRate; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    //  Blockchain getter and setter
    public Map<String, Object> getBlockchainStats() { return blockchainStats; }
    public void setBlockchainStats(Map<String, Object> blockchainStats) { 
        this.blockchainStats = blockchainStats; 
    }
}