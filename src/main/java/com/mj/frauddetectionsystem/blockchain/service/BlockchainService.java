package com.mj.frauddetectionsystem.blockchain.service;

import com.mj.frauddetectionsystem.blockchain.model.FraudBlock;
import com.mj.frauddetectionsystem.blockchain.repository.BlockchainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

@Service
public class BlockchainService {
    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);
    
    @Autowired
    private BlockchainRepository blockchainRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${blockchain.mining.difficulty:4}")
    private int miningDifficulty;
    
    @Transactional
    public FraudBlock initializeBlockchain() {
        Optional<FraudBlock> lastBlock = blockchainRepository.findTopByOrderByBlockIndexDesc();
        if (lastBlock.isEmpty()) {
            logger.info("Initializing blockchain with genesis block");
            FraudBlock genesisBlock = new FraudBlock(
                0L, "Genesis Block - Fraud Detection System Initialized", "0",
                "GENESIS", "APPROVED", BigDecimal.ZERO, "System Initialization"
            );
            genesisBlock.setCurrentHash(genesisBlock.calculateHash());
            genesisBlock.setValidatorSignature("SYSTEM");
            return blockchainRepository.save(genesisBlock);
        }
        logger.info("Blockchain already initialized with {} blocks", blockchainRepository.count());
        return lastBlock.get();
    }
    
    @Transactional
    public FraudBlock addFraudBlock(String transactionId, Map<String, Object> transactionData,
                                   String fraudDecision, BigDecimal riskScore, List<String> fraudReasons) {
        try {
            logger.debug("Adding fraud block for transaction: {}", transactionId);
            FraudBlock previousBlock = blockchainRepository.findTopByOrderByBlockIndexDesc()
                .orElseGet(this::initializeBlockchain);
            String transactionJson = objectMapper.writeValueAsString(transactionData);
            String reasonsString = String.join(", ", fraudReasons);
            FraudBlock newBlock = new FraudBlock(
                previousBlock.getBlockIndex() + 1, transactionJson,
                previousBlock.getCurrentHash(), transactionId,
                fraudDecision, riskScore, reasonsString
            );
            newBlock.setCurrentHash(newBlock.calculateHash());
            newBlock.mineBlock(miningDifficulty);
            newBlock.setValidatorSignature("VALIDATOR_" + Math.abs((newBlock.getBlockIndex() + newBlock.getCurrentHash()).hashCode()));
            FraudBlock savedBlock = blockchainRepository.save(newBlock);
            logger.info("Block {} added to blockchain for transaction {}", savedBlock.getBlockIndex(), transactionId);
            return savedBlock;
        } catch (Exception e) {
            logger.error("Failed to add block to blockchain: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add block to blockchain", e);
        }
    }
    
    public boolean validateBlockchain() {
        List<FraudBlock> blocks = blockchainRepository.findTop100ByOrderByBlockIndexDesc();
        if (blocks.isEmpty()) return true;
        Collections.reverse(blocks);
        for (int i = 1; i < blocks.size(); i++) {
            FraudBlock currentBlock = blocks.get(i);
            FraudBlock previousBlock = blocks.get(i - 1);
            if (!currentBlock.getCurrentHash().equals(currentBlock.calculateHash())) {
                logger.error("Current hash invalid at block {}", currentBlock.getBlockIndex());
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())) {
                logger.error("Previous hash mismatch at block {}", currentBlock.getBlockIndex());
                return false;
            }
        }
        logger.info("Blockchain validation successful");
        return true;
    }
    
    public List<FraudBlock> getTransactionHistory(String transactionId) {
        return blockchainRepository.findByTransactionId(transactionId);
    }
    
    public Map<String, Object> getBlockchainStats() {
        Map<String, Object> stats = new HashMap<>();
        long totalBlocks = blockchainRepository.count();
        long invalidBlocks = blockchainRepository.countInvalidBlocks();
        long blockedCount = blockchainRepository.countBlockedTransactions();
        boolean isValid = validateBlockchain();
        Optional<FraudBlock> lastBlock = blockchainRepository.findTopByOrderByBlockIndexDesc();
        stats.put("totalBlocks", totalBlocks);
        stats.put("invalidBlocks", invalidBlocks);
        stats.put("blockedTransactions", blockedCount);
        stats.put("blockchainValid", isValid);
        stats.put("lastBlockIndex", lastBlock.map(FraudBlock::getBlockIndex).orElse(0L));
        stats.put("lastBlockHash", lastBlock.map(FraudBlock::getCurrentHash).orElse("N/A"));
        stats.put("lastBlockTimestamp", lastBlock.map(FraudBlock::getTimestamp).orElse(null));
        return stats;
    }
    
    public List<FraudBlock> getHighRiskBlocks(BigDecimal minRiskScore) {
        return blockchainRepository.findHighRiskBlocks(minRiskScore);
    }
}