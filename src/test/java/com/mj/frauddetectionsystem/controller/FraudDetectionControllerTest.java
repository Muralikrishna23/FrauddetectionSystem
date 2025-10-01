package com.mj.frauddetectionsystem.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.frauddetectionsystem.dto.FraudDetectionResponse;
import com.mj.frauddetectionsystem.dto.TransactionRequest;
import com.mj.frauddetectionsystem.service.FraudDetectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FraudDetectionController.class)
public class FraudDetectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    public void testAnalyzeTransaction_ValidRequest_ReturnsOk() throws Exception {
        // Arrange
        TransactionRequest request = createValidTransactionRequest();
        FraudDetectionResponse response = new FraudDetectionResponse(
            "TEST001", false, BigDecimal.ZERO, Arrays.asList()
        );
        
        when(fraudDetectionService.processTransaction(any(TransactionRequest.class)))
            .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/fraud-detection/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TEST001"))
                .andExpect(jsonPath("$.isFraudulent").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAnalyzeTransaction_FraudulentTransaction_ReturnsFraudDetected() throws Exception {
        // Arrange
        TransactionRequest request = createHighAmountTransactionRequest();
        FraudDetectionResponse response = new FraudDetectionResponse(
            "FRAUD001", true, new BigDecimal("0.85"), Arrays.asList("Amount-Based Rule")
        );
        
        when(fraudDetectionService.processTransaction(any(TransactionRequest.class)))
            .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/fraud-detection/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("FRAUD001"))
                .andExpect(jsonPath("$.isFraudulent").value(true))
                .andExpect(jsonPath("$.confidenceScore").value(0.85));
    }

    @Test
    public void testAnalyzeTransaction_Unauthorized_Returns401() throws Exception {
        TransactionRequest request = createValidTransactionRequest();
        
        mockMvc.perform(post("/fraud-detection/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAnalyzeTransaction_InvalidRequest_Returns400() throws Exception {
        TransactionRequest request = new TransactionRequest();
        // Missing required fields
        
        mockMvc.perform(post("/fraud-detection/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private TransactionRequest createValidTransactionRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setTransactionId("TEST001");
        request.setUserId("user001");
        request.setAmount(new BigDecimal("100.00"));
        request.setMerchantCategoryCode("GRO");
        request.setMerchantName("Test Store");
        request.setLocation("USA");
        request.setPaymentMethod("CREDIT_CARD");
        request.setDescription("Test transaction");
        return request;
    }

    private TransactionRequest createHighAmountTransactionRequest() {
        TransactionRequest request = createValidTransactionRequest();
        request.setTransactionId("FRAUD001");
        request.setAmount(new BigDecimal("15000.00"));
        request.setMerchantCategoryCode("JEW");
        return request;
    }
}