
-- Drop existing tables if they exist
DROP TABLE IF EXISTS fraud_alerts;
DROP TABLE IF EXISTS fraud_blocks;
DROP TABLE IF EXISTS smart_contracts;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS merchant_categories;
DROP TABLE IF EXISTS users;

-- ============================================
-- Table 1: users
-- ============================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    
    INDEX idx_user_id (user_id),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table 2: user_profiles
-- ============================================
CREATE TABLE user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    average_monthly_spending DECIMAL(10, 2),
    typical_spending_locations VARCHAR(255),
    preferred_payment_methods VARCHAR(255),
    risk_score DECIMAL(5, 2) DEFAULT 0.00,
    occupation VARCHAR(255),
    income_range VARCHAR(255),
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table 3: merchant_categories
-- ============================================
CREATE TABLE merchant_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(255) NOT NULL UNIQUE,
    category_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    risk_level VARCHAR(20) DEFAULT 'LOW',
    
    INDEX idx_category_code (category_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table 4: transactions
-- ============================================
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    merchant_category_id BIGINT,
    merchant_name VARCHAR(255),
    location VARCHAR(255),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50),
    is_fraudulent BOOLEAN DEFAULT FALSE,
    fraud_score DECIMAL(5, 2) DEFAULT 0.00,
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    description VARCHAR(255),
    
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (merchant_category_id) REFERENCES merchant_categories(id),
    
    INDEX idx_user_timestamp (user_id, timestamp),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_amount (amount),
    INDEX idx_is_fraudulent (is_fraudulent)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table 5: fraud_alerts
-- ============================================
CREATE TABLE fraud_alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    transaction_id BIGINT,
    alert_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    alert_type VARCHAR(100) NOT NULL,
    description TEXT,
    triggered_rules TEXT,
    severity VARCHAR(20) DEFAULT 'MEDIUM',
    confidence_score DECIMAL(5, 2),
    status VARCHAR(50) DEFAULT 'OPEN',
    resolved_at TIMESTAMP NULL,
    resolved_by VARCHAR(255),
    
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id),
    
    INDEX idx_user_alert_time (user_id, alert_time),
    INDEX idx_severity (severity),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table 6: fraud_blocks (BLOCKCHAIN)
-- ============================================
CREATE TABLE fraud_blocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_index BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transaction_data TEXT NOT NULL,
    previous_hash VARCHAR(255) NOT NULL,
    current_hash VARCHAR(255) NOT NULL,
    nonce BIGINT NOT NULL,
    fraud_decision VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    risk_score DECIMAL(5, 2) NOT NULL,
    fraud_reasons TEXT,
    validator_signature VARCHAR(255) NOT NULL,
    is_valid BOOLEAN NOT NULL DEFAULT TRUE,
    
    INDEX idx_block_index (block_index),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_current_hash (current_hash),
    INDEX idx_fraud_decision (fraud_decision)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- Table 7: smart_contracts (BLOCKCHAIN)
-- ============================================
CREATE TABLE smart_contracts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contract_id VARCHAR(50) NOT NULL UNIQUE,
    contract_name VARCHAR(255) NOT NULL,
    contract_rules TEXT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    risk_threshold DECIMAL(5, 2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_executed_at TIMESTAMP NULL,
    execution_count INT NOT NULL DEFAULT 0,
    description TEXT,
    
    INDEX idx_contract_id (contract_id),
    INDEX idx_is_active (is_active),
    INDEX idx_action_type (action_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;