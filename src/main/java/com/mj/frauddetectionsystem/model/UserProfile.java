package com.mj.frauddetectionsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "average_monthly_spending", precision = 10, scale = 2)
    private BigDecimal averageMonthlySpending;
    
    @Column(name = "typical_spending_locations")
    private String typicalSpendingLocations;
    
    @Column(name = "preferred_payment_methods")
    private String preferredPaymentMethods;
    
    @Column(name = "risk_score", precision = 5, scale = 2)
    private BigDecimal riskScore = BigDecimal.ZERO;
    
    private String occupation;
    
    @Column(name = "income_range")
    private String incomeRange;

    // Constructors
    public UserProfile() {}
    
    public UserProfile(User user) {
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public BigDecimal getAverageMonthlySpending() { return averageMonthlySpending; }
    public void setAverageMonthlySpending(BigDecimal averageMonthlySpending) { 
        this.averageMonthlySpending = averageMonthlySpending; 
    }
    
    public String getTypicalSpendingLocations() { return typicalSpendingLocations; }
    public void setTypicalSpendingLocations(String typicalSpendingLocations) { 
        this.typicalSpendingLocations = typicalSpendingLocations; 
    }
    
    public String getPreferredPaymentMethods() { return preferredPaymentMethods; }
    public void setPreferredPaymentMethods(String preferredPaymentMethods) { 
        this.preferredPaymentMethods = preferredPaymentMethods; 
    }
    
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getIncomeRange() { return incomeRange; }
    public void setIncomeRange(String incomeRange) { this.incomeRange = incomeRange; }
}
