package com.mj.frauddetectionsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_user_timestamp", columnList = "user_id, timestamp"),
    @Index(name = "idx_transaction_id", columnList = "transaction_id"),
    @Index(name = "idx_amount", columnList = "amount")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_category_id")
    private MerchantCategory merchantCategory;
    
    @Column(name = "merchant_name")
    private String merchantName;
    private String location;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    
    @Column(name = "is_fraudulent")
    private boolean isFraudulent = false;
    
    @Column(name = "fraud_score", precision = 5, scale = 2)
    private BigDecimal fraudScore = BigDecimal.ZERO;
    
    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;
    
    private String description;
    
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FraudAlert> fraudAlerts = new ArrayList<>();

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, DIGITAL_WALLET, CASH, CHECK
    }
    
    public enum ProcessingStatus {
        PENDING, APPROVED, DECLINED, UNDER_REVIEW
    }

    // Constructors
    public Transaction() {}
    
    public Transaction(String transactionId, User user, BigDecimal amount, 
                      MerchantCategory merchantCategory, String location, 
                      LocalDateTime timestamp, PaymentMethod paymentMethod) {
        this.transactionId = transactionId;
        this.user = user;
        this.amount = amount;
        this.merchantCategory = merchantCategory;
        this.location = location;
        this.timestamp = timestamp;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public MerchantCategory getMerchantCategory() { return merchantCategory; }
    public void setMerchantCategory(MerchantCategory merchantCategory) { this.merchantCategory = merchantCategory; }
    
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public boolean isFraudulent() { return isFraudulent; }
    public void setFraudulent(boolean fraudulent) { isFraudulent = fraudulent; }
    
    public BigDecimal getFraudScore() { return fraudScore; }
    public void setFraudScore(BigDecimal fraudScore) { this.fraudScore = fraudScore; }
    
    public ProcessingStatus getProcessingStatus() { return processingStatus; }
    public void setProcessingStatus(ProcessingStatus processingStatus) { this.processingStatus = processingStatus; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<FraudAlert> getFraudAlerts() { return fraudAlerts; }
    public void setFraudAlerts(List<FraudAlert> fraudAlerts) { this.fraudAlerts = fraudAlerts; }

    @Override
    public String toString() {
        return String.format("Transaction{id=%d, txnId='%s', userId='%s', amount=%.2f, fraudulent=%s}", 
                id, transactionId, user != null ? user.getId() : "N/A", 
                amount, isFraudulent);
    }
}