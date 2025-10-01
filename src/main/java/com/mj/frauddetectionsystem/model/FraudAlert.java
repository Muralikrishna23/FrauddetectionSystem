package com.mj.frauddetectionsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts", indexes = {
    @Index(name = "idx_user_alert_time", columnList = "user_id, alert_time"),
    @Index(name = "idx_severity", columnList = "severity")
})
public class FraudAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
    
    @Column(name = "alert_time", nullable = false)
    private LocalDateTime alertTime;
    
    @Column(name = "alert_type", nullable = false)
    private String alertType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "triggered_rules", columnDefinition = "TEXT")
    private String triggeredRules;
    
    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.MEDIUM;
    
    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;
    
    @Enumerated(EnumType.STRING)
    private AlertStatus status = AlertStatus.OPEN;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "resolved_by")
    private String resolvedBy;

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum AlertStatus {
        OPEN, INVESTIGATING, RESOLVED, FALSE_POSITIVE
    }

    // Constructors
    public FraudAlert() {}
    
    public FraudAlert(User user, Transaction transaction, String alertType, 
                     String description, Severity severity) {
        this.user = user;
        this.transaction = transaction;
        this.alertType = alertType;
        this.description = description;
        this.severity = severity;
        this.alertTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
    
    public LocalDateTime getAlertTime() { return alertTime; }
    public void setAlertTime(LocalDateTime alertTime) { this.alertTime = alertTime; }
    
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getTriggeredRules() { return triggeredRules; }
    public void setTriggeredRules(String triggeredRules) { this.triggeredRules = triggeredRules; }
    
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    
    public AlertStatus getStatus() { return status; }
    public void setStatus(AlertStatus status) { this.status = status; }
    
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    
    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }

    @Override
    public String toString() {
        return String.format("FraudAlert{id=%d, type='%s', severity=%s, status=%s, time=%s}", 
                id, alertType, severity, status, alertTime);
    }
}