package com.mj.frauddetectionsystem.model;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "merchant_categories")
public class MerchantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "category_code", unique = true, nullable = false)
    private String categoryCode;
    
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    
    private String description;
    
    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel = RiskLevel.LOW;
    
    @OneToMany(mappedBy = "merchantCategory", fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    // Constructors
    public MerchantCategory() {}
    
    public MerchantCategory(String categoryCode, String categoryName, RiskLevel riskLevel) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.riskLevel = riskLevel;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}
