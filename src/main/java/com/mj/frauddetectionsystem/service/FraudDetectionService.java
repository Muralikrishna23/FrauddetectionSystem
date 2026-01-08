// ===========================================
// FraudDetectionService.java - Complete Implementation
// ===========================================

package com.mj.frauddetectionsystem.service;

import com.mj.frauddetectionsystem.dto.*;
import com.mj.frauddetectionsystem.model.*;
import com.mj.frauddetectionsystem.repository.*;
import com.mj.frauddetectionsystem.rules.FraudDetectionEngine;
import com.mj.frauddetectionsystem.rules.FraudDetectionResult;
import com.mj.frauddetectionsystem.exception.FraudDetectionException;
import com.mj.frauddetectionsystem.exception.UserNotFoundException;
import com.mj.frauddetectionsystem.exception.MerchantCategoryNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.StringUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
@Transactional
public class FraudDetectionService {

    private static final Logger logger = LoggerFactory.getLogger(FraudDetectionService.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MerchantCategoryRepository merchantCategoryRepository;
    
    @Autowired
    private FraudAlertRepository fraudAlertRepository;
    
  
    @Autowired
    private FraudDetectionEngine fraudDetectionEngine;
    
  
    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    
    @Value("${fraud.detection.alerts.kafka.enabled:false}")
    private boolean kafkaAlertsEnabled;

    @Value("${fraud.detection.alerts.kafka.topic:fraud-alerts}")
    private String fraudAlertsTopic;
    
    @Value("${fraud.detection.alerts.email.enabled:false}")
    private boolean emailAlertsEnabled;
    
    @Value("${fraud.detection.ml.enabled:true}")
    private boolean mlEnabled;
    
    @Value("${fraud.detection.ml.confidence-threshold:0.5}")
    private BigDecimal mlConfidenceThreshold;

   

    /**
     * Process a single transaction for fraud detection
     * 
     * @param request Transaction request to analyze
     * @return Fraud detection response with analysis results
     * @throws FraudDetectionException if processing fails
     */
    public FraudDetectionResponse processTransaction(@Valid @NotNull TransactionRequest request) {
        long startTime = System.currentTimeMillis();
        
        logger.info("Processing transaction: {} for user: {}", request.getTransactionId(), request.getUserId());
        
        try {
            // Validate request
            validateTransactionRequest(request);
            
            // Find user
            User user = findUserByUserId(request.getUserId());
            
            // Find merchant category
            MerchantCategory category = findMerchantCategoryByCode(request.getMerchantCategoryCode());
            
            // Create transaction entity
            Transaction transaction = createTransactionEntity(request, user, category);
            
            // Analyze for fraud using the detection engine
            FraudDetectionResult analysisResult = fraudDetectionEngine.analyzeTransaction(transaction);
            
            // Update transaction with analysis results
            updateTransactionWithResults(transaction, analysisResult);
            
            // Save transaction to database
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Handle fraud detection actions
            if (analysisResult.isFraudulent()) {
                handleFraudDetected(savedTransaction, analysisResult);
            } else {
                handleLegitimateTransaction(savedTransaction);
            }
            
            // Update user profile based on transaction
            updateUserProfile(user, savedTransaction);
            
            // Calculate processing time
            long processingTime = System.currentTimeMillis() - startTime;
            
            // Create and return response
            FraudDetectionResponse response = buildFraudDetectionResponse(savedTransaction, analysisResult);
            response.setProcessingTimeMs(processingTime);
            
            logger.info("Transaction {} processed in {}ms - Result: {}", 
                       request.getTransactionId(), processingTime, 
                       analysisResult.isFraudulent() ? "FRAUDULENT" : "LEGITIMATE");
            
            return response;
            
        } catch (UserNotFoundException | MerchantCategoryNotFoundException e) {
            logger.error("Validation error processing transaction {}: {}", request.getTransactionId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error processing transaction {}: {}", request.getTransactionId(), e.getMessage(), e);
            throw new FraudDetectionException("Failed to process transaction: " + e.getMessage(), e);
        }
    }

    /**
     * Process multiple transactions in batch
     * 
     * @param requests List of transaction requests
     * @return List of fraud detection responses
     */
    @Async
    public CompletableFuture<List<FraudDetectionResponse>> processBatchTransactions(
            @NotNull List<TransactionRequest> requests) {
        
        logger.info("Processing batch of {} transactions", requests.size());
        
        List<FraudDetectionResponse> responses = new ArrayList<>();
        int processedCount = 0;
        int fraudCount = 0;
        
        for (TransactionRequest request : requests) {
            try {
                FraudDetectionResponse response = processTransaction(request);
                responses.add(response);
                processedCount++;
                
                if (response.isFraudulent()) {
                    fraudCount++;
                }
                
            } catch (Exception e) {
                logger.error("Error processing transaction {} in batch: {}", 
                           request.getTransactionId(), e.getMessage());
                
                // Create error response
                FraudDetectionResponse errorResponse = new FraudDetectionResponse();
                errorResponse.setTransactionId(request.getTransactionId());
                errorResponse.setStatus("ERROR");
                errorResponse.setFraudulent(false);
                errorResponse.setConfidenceScore(BigDecimal.ZERO);
                errorResponse.setTriggeredRules(Arrays.asList("Processing Error: " + e.getMessage()));
                
                responses.add(errorResponse);
            }
        }
        
        logger.info("Batch processing completed: {} processed, {} fraudulent", processedCount, fraudCount);
        
        return CompletableFuture.completedFuture(responses);
    }

  
    /**
     * Get all transactions for a specific user (cached)
     * 
     * @param userId User identifier
     * @return List of user transactions
     */
    @Cacheable(value = "user-transactions", key = "#userId")
    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(@NotBlank String userId) {
        logger.debug("Retrieving transactions for user: {}", userId);
        
        User user = findUserByUserId(userId);
        List<Transaction> transactions = transactionRepository.findByUserOrderByTimestampDesc(user);
        
        logger.info("Found {} transactions for user: {}", transactions.size(), userId);
        return transactions;
    }

    /**
     * Get paginated transactions for a user
     * 
     * @param userId User identifier
     * @param pageable Pagination parameters
     * @return Page of transactions
     */
    @Transactional(readOnly = true)
    public Page<Transaction> getUserTransactions(@NotBlank String userId, @NotNull Pageable pageable) {
        User user = findUserByUserId(userId);
        return transactionRepository.findByUserOrderByTimestampDesc(user, pageable);
    }

    /**
     * Get recent transactions for a user within specified time window
     * 
     * @param userId User identifier
     * @param hours Number of hours to look back
     * @return List of recent transactions
     */
    @Transactional(readOnly = true)
    public List<Transaction> getRecentUserTransactions(@NotBlank String userId, int hours) {
        User user = findUserByUserId(userId);
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(hours);
        
        return transactionRepository.findUserTransactionsAfter(user, cutoffTime);
    }

    /**
     * Get fraud alerts for a specific user
     * 
     * @param userId User identifier
     * @return List of fraud alerts
     */
    @Transactional(readOnly = true)
    public List<FraudAlert> getUserAlerts(@NotBlank String userId) {
        logger.debug("Retrieving fraud alerts for user: {}", userId);
        
        User user = findUserByUserId(userId);
        List<FraudAlert> alerts = fraudAlertRepository.findByUserOrderByAlertTimeDesc(user);
        
        logger.info("Found {} fraud alerts for user: {}", alerts.size(), userId);
        return alerts;
    }

    /**
     * Get all open fraud alerts across the system
     * 
     * @return List of open fraud alerts
     */
    @Transactional(readOnly = true)
    public List<FraudAlert> getOpenAlerts() {
        logger.debug("Retrieving all open fraud alerts");
        
        List<FraudAlert> openAlerts = fraudAlertRepository.findOpenAlerts();
        
        logger.info("Found {} open fraud alerts", openAlerts.size());
        return openAlerts;
    }

    /**
     * Get fraud alerts by severity level
     * 
     * @param severity Alert severity
     * @return List of alerts with specified severity
     */
    @Transactional(readOnly = true)
    public List<FraudAlert> getAlertsBySeverity(@NotNull FraudAlert.Severity severity) {
        return fraudAlertRepository.findBySeverity(severity);
    }

    /**
     * Resolve a fraud alert
     * 
     * @param alertId Alert identifier
     * @param resolvedBy Who resolved the alert
     * @param resolution Resolution notes
     */
    @Transactional
    public void resolveFraudAlert(@NotNull Long alertId, @NotBlank String resolvedBy, String resolution) {
        FraudAlert alert = fraudAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Fraud alert not found: " + alertId));
        
        alert.setStatus(FraudAlert.AlertStatus.RESOLVED);
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolvedBy(resolvedBy);
        
        if (StringUtils.hasText(resolution)) {
            alert.setDescription(alert.getDescription() + " | Resolution: " + resolution);
        }
        
        fraudAlertRepository.save(alert);
        
        logger.info("Fraud alert {} resolved by {}", alertId, resolvedBy);
    }

 
    /**
     * Get comprehensive fraud detection statistics
     * 
     * @return Fraud statistics object
     */
    @Cacheable(value = "fraud-statistics", unless = "#result == null")
    @Transactional(readOnly = true)
    public FraudStatistics getFraudStatistics() {
        logger.debug("Calculating fraud detection statistics");
        
        try {
            // Overall statistics
            long totalTransactions = transactionRepository.count();
            List<Transaction> fraudulentTransactions = transactionRepository.findByIsFraudulentTrueOrderByTimestampDesc();
            long fraudulentCount = fraudulentTransactions.size();
            
            // Today's statistics
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime todayEnd = todayStart.plusDays(1);
            
            long todayTotal = transactionRepository.countTransactionsInTimeRange(todayStart, todayEnd);
            long todayFraud = transactionRepository.countFraudulentTransactionsInTimeRange(todayStart, todayEnd);
            
            // This week's statistics
            LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
            long weekTotal = transactionRepository.countTransactionsInTimeRange(weekStart, LocalDateTime.now());
            long weekFraud = transactionRepository.countFraudulentTransactionsInTimeRange(weekStart, LocalDateTime.now());
            
            // Create comprehensive statistics
            FraudStatistics stats = new FraudStatistics(totalTransactions, fraudulentCount, todayTotal, todayFraud);
            
            // Add weekly statistics if available
            if (weekTotal > 0) {
                BigDecimal weekFraudRate = new BigDecimal(weekFraud)
                        .divide(new BigDecimal(weekTotal), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                stats.setFraudulentTransactions(weekTotal);
                stats.setTodayFraudulent(weekFraud);
                stats.setFraudRate(weekFraudRate);
            }
            
            // Add alert statistics
            long openAlerts = fraudAlertRepository.countOpenAlertsBySeverity(null);
            long criticalAlerts = fraudAlertRepository.countOpenAlertsBySeverity(FraudAlert.Severity.CRITICAL);
            
            //stats.setOpenAlertsCount(openAlerts);
            //stats.setCriticalAlertsCount(criticalAlerts);
            
            // Add average processing metrics
            if (totalTransactions > 0) {
                BigDecimal avgAmount = fraudulentTransactions.stream()
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(fraudulentCount > 0 ? fraudulentCount : 1), 2, RoundingMode.HALF_UP);
                
                //stats.setAverageFraudAmount(avgAmount);
            }
            
            logger.info("Fraud statistics calculated: {} total, {} fraudulent ({}%)", 
                       totalTransactions, fraudulentCount, stats.getFraudRate());
            
            return stats;
            
        } catch (Exception e) {
            logger.error("Error calculating fraud statistics: {}", e.getMessage(), e);
            throw new FraudDetectionException("Failed to calculate fraud statistics", e);
        }
    }

    /**
     * Get fraud statistics for a specific time period
     * 
     * @param startDate Start of period
     * @param endDate End of period
     * @return Period-specific fraud statistics
     */
    @Transactional(readOnly = true)
    public FraudStatistics getFraudStatisticsForPeriod(@NotNull LocalDateTime startDate, @NotNull LocalDateTime endDate) {
        long totalTransactions = transactionRepository.countTransactionsInTimeRange(startDate, endDate);
        long fraudulentTransactions = transactionRepository.countFraudulentTransactionsInTimeRange(startDate, endDate);
        
        return new FraudStatistics(totalTransactions, fraudulentTransactions, totalTransactions, fraudulentTransactions);
    }

    
    /**
     * Update user profile based on transaction patterns
     * 
     * @param user User entity
     * @param transaction Latest transaction
     */
    @CacheEvict(value = "user-transactions", key = "#user.userId")
    private void updateUserProfile(@NotNull User user, @NotNull Transaction transaction) {
        try {
            UserProfile profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);
                user.setProfile(profile);
            }
            
            
            BigDecimal currentAverage = transactionRepository.getAverageTransactionAmount(user);
            if (currentAverage != null) {
                profile.setAverageMonthlySpending(currentAverage.multiply(new BigDecimal("30"))); // Rough monthly estimate
            }
            
           
            List<Transaction> recentTransactions = getRecentUserTransactions(user.getUserId(), 24 * 30); // Last 30 days
            long recentFraudCount = recentTransactions.stream().mapToLong(t -> t.isFraudulent() ? 1 : 0).sum();
            
            if (recentTransactions.size() > 0) {
                BigDecimal riskScore = new BigDecimal(recentFraudCount)
                        .divide(new BigDecimal(recentTransactions.size()), 4, RoundingMode.HALF_UP);
                profile.setRiskScore(riskScore);
            }
            
           
            updateUserPreferences(profile, recentTransactions);
            
            logger.debug("Updated user profile for user: {}", user.getUserId());
            
        } catch (Exception e) {
            logger.warn("Error updating user profile for user {}: {}", user.getUserId(), e.getMessage());
        }
    }

   
    /**
     * Validate transaction request
     */
    private void validateTransactionRequest(@NotNull TransactionRequest request) {
        if (!StringUtils.hasText(request.getTransactionId())) {
            throw new IllegalArgumentException("Transaction ID is required");
        }
        
        if (!StringUtils.hasText(request.getUserId())) {
            throw new IllegalArgumentException("User ID is required");
        }
        
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        if (!StringUtils.hasText(request.getMerchantCategoryCode())) {
            throw new IllegalArgumentException("Merchant category code is required");
        }
        
        // Check for duplicate transaction ID
        if (transactionRepository.findByTransactionId(request.getTransactionId()).isPresent()) {
            throw new IllegalArgumentException("Duplicate transaction ID: " + request.getTransactionId());
        }
    }

    /**
     * Find user by user ID
     */
    private User findUserByUserId(@NotBlank String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    /**
     * Find merchant category by code
     */
    private MerchantCategory findMerchantCategoryByCode(@NotBlank String categoryCode) {
        return merchantCategoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new MerchantCategoryNotFoundException("Merchant category not found: " + categoryCode));
    }

    /**
     * Create transaction entity from request
     */
    private Transaction createTransactionEntity(@NotNull TransactionRequest request, 
                                               @NotNull User user, 
                                               @NotNull MerchantCategory category) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setMerchantCategory(category);
        transaction.setMerchantName(request.getMerchantName());
        transaction.setLocation(request.getLocation());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(request.getDescription());
        transaction.setProcessingStatus(Transaction.ProcessingStatus.PENDING);
        
        // Parse payment method
        try {
            transaction.setPaymentMethod(Transaction.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid payment method {}, defaulting to CREDIT_CARD", request.getPaymentMethod());
            transaction.setPaymentMethod(Transaction.PaymentMethod.CREDIT_CARD);
        }
        
        return transaction;
    }

    /**
     * Update transaction with fraud analysis results
     */
    private void updateTransactionWithResults(@NotNull Transaction transaction, @NotNull FraudDetectionResult result) {
        transaction.setFraudulent(result.isFraudulent());
        transaction.setFraudScore(result.getConfidenceScore());
        
        if (result.isFraudulent()) {
            transaction.setProcessingStatus(Transaction.ProcessingStatus.UNDER_REVIEW);
        } else {
            transaction.setProcessingStatus(Transaction.ProcessingStatus.APPROVED);
        }
    }

    /**
     * Handle actions when fraud is detected
     */
    private void handleFraudDetected(@NotNull Transaction transaction, @NotNull FraudDetectionResult result) {
        logger.warn("FRAUD DETECTED - Transaction: {}, User: {}, Amount: ${}, Confidence: {}", 
                   transaction.getTransactionId(), transaction.getUser().getUserId(), 
                   transaction.getAmount(), result.getConfidenceScore());
        
        // Create fraud alert
        createFraudAlert(transaction, result);
        
        // Publish to Kafka if enabled
        if (kafkaAlertsEnabled) {
            publishFraudAlert(transaction, result);
        }
        
        // Send email alert if enabled
        if (emailAlertsEnabled) {
            sendEmailAlert(transaction, result);
        }
        
        // Update user risk score
        incrementUserRiskScore(transaction.getUser());
    }

    /**
     * Handle legitimate transactions
     */
    private void handleLegitimateTransaction(@NotNull Transaction transaction) {
        logger.debug("Transaction {} approved for user: {}", 
                    transaction.getTransactionId(), transaction.getUser().getUserId());
        
        // Optionally decrease user risk score for good behavior
        decrementUserRiskScore(transaction.getUser());
    }

    /**
     * Create fraud alert
     */
    private void createFraudAlert(@NotNull Transaction transaction, @NotNull FraudDetectionResult result) {
        try {
            FraudAlert alert = new FraudAlert();
            alert.setUser(transaction.getUser());
            alert.setTransaction(transaction);
            alert.setAlertType("FRAUD_DETECTION");
            alert.setDescription(String.format("Transaction %s flagged by fraud detection system. Rules: %s", 
                               transaction.getTransactionId(), String.join(", ", result.getTriggeredRules())));
            alert.setSeverity(determineSeverity(result.getConfidenceScore()));
            alert.setTriggeredRules(String.join(", ", result.getTriggeredRules()));
            alert.setConfidenceScore(result.getConfidenceScore());
            alert.setAlertTime(LocalDateTime.now());
            alert.setStatus(FraudAlert.AlertStatus.OPEN);
            
            fraudAlertRepository.save(alert);
            
            logger.info("Fraud alert created for transaction: {} with confidence: {}", 
                       transaction.getTransactionId(), result.getConfidenceScore());
            
        } catch (Exception e) {
            logger.error("Error creating fraud alert for transaction {}: {}", 
                        transaction.getTransactionId(), e.getMessage(), e);
        }
    }

    /**
     * Publish fraud alert to Kafka
     */
    private void publishFraudAlert(@NotNull Transaction transaction, @NotNull FraudDetectionResult result) {
        try {
            if (kafkaTemplate != null) {
                FraudAlertMessage message = new FraudAlertMessage(
                    transaction.getTransactionId(),
                    transaction.getUser().getUserId(),
                    transaction.getAmount(),
                    result.getConfidenceScore(),
                    result.getTriggeredRules()
                );
                
                kafkaTemplate.send(fraudAlertsTopic, message);
                logger.info("Fraud alert published to Kafka for transaction: {}", transaction.getTransactionId());
            }
        } catch (Exception e) {
            logger.error("Failed to publish fraud alert to Kafka: {}", e.getMessage(), e);
        }
    }

    /**
     * Send email alert (placeholder implementation)
     */
    private void sendEmailAlert(@NotNull Transaction transaction, @NotNull FraudDetectionResult result) {
        // TODO: Implement email sending logic
        logger.info("Email alert would be sent for transaction: {}", transaction.getTransactionId());
    }

    /**
     * Determine alert severity based on confidence score
     */
    private FraudAlert.Severity determineSeverity(@NotNull BigDecimal confidenceScore) {
        if (confidenceScore.compareTo(new BigDecimal("0.8")) >= 0) {
            return FraudAlert.Severity.CRITICAL;
        } else if (confidenceScore.compareTo(new BigDecimal("0.6")) >= 0) {
            return FraudAlert.Severity.HIGH;
        } else if (confidenceScore.compareTo(new BigDecimal("0.4")) >= 0) {
            return FraudAlert.Severity.MEDIUM;
        } else {
            return FraudAlert.Severity.LOW;
        }
    }

    /**
     * Build fraud detection response
     */
    private FraudDetectionResponse buildFraudDetectionResponse(@NotNull Transaction transaction, 
                                                             @NotNull FraudDetectionResult result) {
        FraudDetectionResponse response = new FraudDetectionResponse();
        response.setTransactionId(transaction.getTransactionId());
        response.setFraudulent(result.isFraudulent());
        response.setConfidenceScore(result.getConfidenceScore());
        response.setTriggeredRules(result.getTriggeredRules());
        response.setStatus(result.isFraudulent() ? "FLAGGED" : "APPROVED");
        response.setProcessedAt(LocalDateTime.now());
        
        return response;
    }

    /**
     * Update user preferences based on transaction history
     */
    private void updateUserPreferences(@NotNull UserProfile profile, @NotNull List<Transaction> recentTransactions) {
        if (recentTransactions.isEmpty()) return;
        
        // Update typical locations
        Map<String, Long> locationCounts = recentTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getLocation, Collectors.counting()));
        
        String topLocations = locationCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
        
        profile.setTypicalSpendingLocations(topLocations);
        
        // Update preferred payment methods
        Map<Transaction.PaymentMethod, Long> paymentCounts = recentTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod, Collectors.counting()));
        
        String topPaymentMethods = paymentCounts.entrySet().stream()
                .sorted(Map.Entry.<Transaction.PaymentMethod, Long>comparingByValue().reversed())
                .limit(3)
                .map(entry -> entry.getKey().toString())
                .collect(Collectors.joining(","));
        
        profile.setPreferredPaymentMethods(topPaymentMethods);
    }

    /**
     * Increment user risk score after fraud detection
     */
    private void incrementUserRiskScore(@NotNull User user) {
        try {
            UserProfile profile = user.getProfile();
            if (profile != null && profile.getRiskScore() != null) {
                BigDecimal newRiskScore = profile.getRiskScore()
                        .add(new BigDecimal("0.1"))
                        .min(new BigDecimal("1.0")); // Cap at 1.0
                profile.setRiskScore(newRiskScore);
            }
        } catch (Exception e) {
            logger.warn("Error updating risk score for user {}: {}", user.getUserId(), e.getMessage());
        }
    }

    /**
     * Decrement user risk score for good behavior
     */
    private void decrementUserRiskScore(@NotNull User user) {
        try {
            UserProfile profile = user.getProfile();
            if (profile != null && profile.getRiskScore() != null) {
                BigDecimal newRiskScore = profile.getRiskScore()
                        .subtract(new BigDecimal("0.01"))
                        .max(BigDecimal.ZERO); // Floor at 0.0
                profile.setRiskScore(newRiskScore);
            }
        } catch (Exception e) {
            logger.warn("Error updating risk score for user {}: {}", user.getUserId(), e.getMessage());
        }
    }

    /**
     * Clear fraud statistics cache
     */
    @CacheEvict(value = "fraud-statistics", allEntries = true)
    public void clearStatisticsCache() {
        logger.info("Fraud statistics cache cleared");
    }

    /**
     * Clear user transactions cache
     */
    @CacheEvict(value = "user-transactions", allEntries = true)
    public void clearUserTransactionsCache() {
        logger.info("User transactions cache cleared");
    }

    /**
     * Get system health metrics
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSystemHealthMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            metrics.put("totalTransactions", transactionRepository.count());
            metrics.put("totalUsers", userRepository.count());
            metrics.put("openAlerts", fraudAlertRepository.findOpenAlerts().size());
            metrics.put("systemStatus", "HEALTHY");
            metrics.put("lastUpdated", LocalDateTime.now().format(TIMESTAMP_FORMAT));
        } catch (Exception e) {
            metrics.put("systemStatus", "ERROR");
            metrics.put("error", e.getMessage());
        }
        
        return metrics;
    }
}