package com.mj.frauddetectionsystem.blockchain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "smart_contracts")
public class SmartContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contract_id", nullable = false, unique = true)
    private String contractId;
    @Column(name = "contract_name", nullable = false)
    private String contractName;
    @Column(name = "contract_rules", columnDefinition = "TEXT", nullable = false)
    private String contractRules;
    @Column(name = "action_type", nullable = false)
    private String actionType;
    @Column(name = "risk_threshold", precision = 5, scale = 2, nullable = false)
    private BigDecimal riskThreshold;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "last_executed_at")
    private LocalDateTime lastExecutedAt;
    @Column(name = "execution_count", nullable = false)
    private Integer executionCount;
    @Column(columnDefinition = "TEXT")
    private String description;

    public SmartContract() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.executionCount = 0;
    }

    public SmartContract(String contractId, String contractName, String contractRules,
                        String actionType, BigDecimal riskThreshold, String description) {
        this();
        this.contractId = contractId;
        this.contractName = contractName;
        this.contractRules = contractRules;
        this.actionType = actionType;
        this.riskThreshold = riskThreshold;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public String getContractRules() { return contractRules; }
    public void setContractRules(String contractRules) { this.contractRules = contractRules; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public BigDecimal getRiskThreshold() { return riskThreshold; }
    public void setRiskThreshold(BigDecimal riskThreshold) { this.riskThreshold = riskThreshold; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getLastExecutedAt() { return lastExecutedAt; }
    public void setLastExecutedAt(LocalDateTime lastExecutedAt) { this.lastExecutedAt = lastExecutedAt; }
    public Integer getExecutionCount() { return executionCount; }
    public void setExecutionCount(Integer executionCount) { this.executionCount = executionCount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}