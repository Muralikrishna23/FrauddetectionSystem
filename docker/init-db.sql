
-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS frauddb;

-- Use the database
USE frauddb;

-- Create user and grant permissions
CREATE USER IF NOT EXISTS 'fraud_user'@'%' IDENTIFIED BY 'fraud_password';
GRANT ALL PRIVILEGES ON frauddb.* TO 'fraud_user'@'%';

-- Grant permissions for localhost as well
CREATE USER IF NOT EXISTS 'fraud_user'@'localhost' IDENTIFIED BY 'fraud_password';
GRANT ALL PRIVILEGES ON frauddb.* TO 'fraud_user'@'localhost';

-- Flush privileges to ensure changes take effect
FLUSH PRIVILEGES;

-- Set MySQL settings for better performance
SET GLOBAL innodb_buffer_pool_size = 128M;
SET GLOBAL max_connections = 200;
SET GLOBAL query_cache_type = 1;
SET GLOBAL query_cache_size = 32M;

-- Create additional indexes for better performance
-- (These will be created after Spring Boot starts, but we can prepare some)

-- Additional configuration for fraud detection workload
SET GLOBAL innodb_log_file_size = 64M;
SET GLOBAL innodb_flush_log_at_trx_commit = 2;
SET GLOBAL sync_binlog = 0;

-- Create a stored procedure for database statistics
DELIMITER //
CREATE PROCEDURE GetFraudStats()
BEGIN
    SELECT 
        'Total Transactions' as metric,
        COUNT(*) as value
    FROM transactions
    UNION ALL
    SELECT 
        'Fraudulent Transactions' as metric,
        COUNT(*) as value  
    FROM transactions 
    WHERE is_fraudulent = true
    UNION ALL
    SELECT 
        'Open Alerts' as metric,
        COUNT(*) as value
    FROM fraud_alerts 
    WHERE status = 'OPEN'
    UNION ALL
    SELECT 
        'Active Users' as metric,
        COUNT(*) as value
    FROM users 
    WHERE is_active = true;
END //
DELIMITER ;

-- Create a function to calculate fraud rate
DELIMITER //
CREATE FUNCTION GetFraudRate() RETURNS DECIMAL(5,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE total_txns INT DEFAULT 0;
    DECLARE fraud_txns INT DEFAULT 0;
    DECLARE fraud_rate DECIMAL(5,2) DEFAULT 0.00;
    
    SELECT COUNT(*) INTO total_txns FROM transactions;
    SELECT COUNT(*) INTO fraud_txns FROM transactions WHERE is_fraudulent = true;
    
    IF total_txns > 0 THEN
        SET fraud_rate = (fraud_txns * 100.0) / total_txns;
    END IF;
    
    RETURN fraud_rate;
END //
DELIMITER ;

-- Create views for reporting
CREATE OR REPLACE VIEW fraud_summary AS
SELECT 
    u.user_id,
    u.first_name,
    u.last_name,
    u.email,
    COUNT(t.id) as total_transactions,
    SUM(CASE WHEN t.is_fraudulent = true THEN 1 ELSE 0 END) as fraudulent_transactions,
    SUM(t.amount) as total_amount,
    AVG(t.amount) as avg_amount,
    MAX(t.timestamp) as last_transaction
FROM users u
LEFT JOIN transactions t ON u.id = t.user_id
WHERE u.is_active = true
GROUP BY u.id, u.user_id, u.first_name, u.last_name, u.email;

CREATE OR REPLACE VIEW daily_fraud_stats AS
SELECT 
    DATE(timestamp) as transaction_date,
    COUNT(*) as total_transactions,
    SUM(CASE WHEN is_fraudulent = true THEN 1 ELSE 0 END) as fraudulent_transactions,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount,
    (SUM(CASE WHEN is_fraudulent = true THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as fraud_rate_percent
FROM transactions
GROUP BY DATE(timestamp)
ORDER BY transaction_date DESC;

CREATE OR REPLACE VIEW merchant_risk_analysis AS
SELECT 
    mc.category_name,
    mc.risk_level,
    COUNT(t.id) as transaction_count,
    SUM(CASE WHEN t.is_fraudulent = true THEN 1 ELSE 0 END) as fraud_count,
    SUM(t.amount) as total_amount,
    AVG(t.amount) as avg_amount,
    (SUM(CASE WHEN t.is_fraudulent = true THEN 1 ELSE 0 END) * 100.0 / COUNT(t.id)) as actual_fraud_rate
FROM merchant_categories mc
LEFT JOIN transactions t ON mc.id = t.merchant_category_id
GROUP BY mc.id, mc.category_name, mc.risk_level
ORDER BY actual_fraud_rate DESC;

-- Insert some reference data that might be useful
INSERT IGNORE INTO merchant_categories (category_code, category_name, description, risk_level) VALUES
('UNK', 'Unknown', 'Unknown or unclassified merchants', 'CRITICAL'),
('TST', 'Test', 'Test transactions and merchants', 'LOW'),
('SYS', 'System', 'System-generated transactions', 'LOW');

-- Create indexes for better performance (in addition to JPA-generated ones)
-- Note: Some of these may already be created by JPA, but MySQL will ignore duplicates

-- Additional indexes for reporting queries
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(DATE(timestamp));
CREATE INDEX IF NOT EXISTS idx_transactions_amount_fraud ON transactions(amount, is_fraudulent);
CREATE INDEX IF NOT EXISTS idx_fraud_alerts_severity_status ON fraud_alerts(severity, status);
CREATE INDEX IF NOT EXISTS idx_user_profiles_risk_score ON user_profiles(risk_score);

-- Create a table for system configuration
CREATE TABLE IF NOT EXISTS system_config (
    config_key VARCHAR(50) PRIMARY KEY,
    config_value TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default system configuration
INSERT IGNORE INTO system_config (config_key, config_value, description) VALUES
('fraud_threshold_high', '10000.0', 'High amount threshold for fraud detection'),
('fraud_threshold_critical', '50000.0', 'Critical amount threshold for fraud detection'),
('max_transactions_per_hour', '20', 'Maximum transactions per hour per user'),
('alert_retention_days', '90', 'Number of days to retain fraud alerts'),
('transaction_retention_days', '365', 'Number of days to retain transaction history');

-- Log the initialization
INSERT INTO system_config (config_key, config_value, description) VALUES
('db_initialized_at', NOW(), 'Database initialization timestamp')
ON DUPLICATE KEY UPDATE 
config_value = NOW(),
updated_at = CURRENT_TIMESTAMP;

SHOW TABLES;
SELECT 'Database initialized successfully' as status;
