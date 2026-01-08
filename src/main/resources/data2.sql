-- Insert sample users
INSERT INTO users (id, username, email, phone, account_status, created_at, updated_at) 
VALUES 
(1, 'john_doe', 'john.doe@email.com', '+1-555-0101', 'ACTIVE', NOW(), NOW()),
(2, 'jane_smith', 'jane.smith@email.com', '+1-555-0102', 'ACTIVE', NOW(), NOW()),
(3, 'bob_wilson', 'bob.wilson@email.com', '+1-555-0103', 'ACTIVE', NOW(), NOW()),
(4, 'alice_brown', 'alice.brown@email.com', '+1-555-0104', 'ACTIVE', NOW(), NOW()),
(5, 'charlie_davis', 'charlie.davis@email.com', '+1-555-0105', 'SUSPENDED', NOW(), NOW());

-- Insert merchant categories
INSERT INTO merchant_categories (id, category_name, risk_level, created_at) 
VALUES 
(1, 'GROCERY', 'LOW', NOW()),
(2, 'ELECTRONICS', 'MEDIUM', NOW()),
(3, 'JEWELRY', 'HIGH', NOW()),
(4, 'ONLINE_GAMING', 'HIGH', NOW()),
(5, 'RESTAURANT', 'LOW', NOW()),
(6, 'GAS_STATION', 'LOW', NOW()),
(7, 'CRYPTOCURRENCY', 'HIGH', NOW()),
(8, 'TRAVEL', 'MEDIUM', NOW());

-- Insert sample transactions (normal transactions)
INSERT INTO transactions (id, user_id, amount, currency, merchant_name, merchant_category, transaction_type, status, location, ip_address, device_id, timestamp, created_at) 
VALUES 
(1, 1, 45.99, 'USD', 'Walmart', 'GROCERY', 'PURCHASE', 'COMPLETED', 'New York, USA', '192.168.1.1', 'DEVICE001', NOW(), NOW()),
(2, 1, 89.50, 'USD', 'Best Buy', 'ELECTRONICS', 'PURCHASE', 'COMPLETED', 'New York, USA', '192.168.1.1', 'DEVICE001', NOW(), NOW()),
(3, 2, 25.00, 'USD', 'Starbucks', 'RESTAURANT', 'PURCHASE', 'COMPLETED', 'Los Angeles, USA', '192.168.2.1', 'DEVICE002', NOW(), NOW()),
(4, 2, 120.75, 'USD', 'Shell Gas Station', 'GAS_STATION', 'PURCHASE', 'COMPLETED', 'Los Angeles, USA', '192.168.2.1', 'DEVICE002', NOW(), NOW()),
(5, 3, 199.99, 'USD', 'Amazon', 'ELECTRONICS', 'PURCHASE', 'COMPLETED', 'Chicago, USA', '192.168.3.1', 'DEVICE003', NOW(), NOW());

-- Insert suspicious/fraudulent transactions
INSERT INTO transactions (id, user_id, amount, currency, merchant_name, merchant_category, transaction_type, status, location, ip_address, device_id, timestamp, created_at) 
VALUES 
(6, 1, 9999.99, 'USD', 'Luxury Jewelers', 'JEWELRY', 'PURCHASE', 'FLAGGED', 'Moscow, Russia', '45.67.89.10', 'DEVICE999', NOW(), NOW()),
(7, 2, 5000.00, 'USD', 'Crypto Exchange', 'CRYPTOCURRENCY', 'PURCHASE', 'FLAGGED', 'Lagos, Nigeria', '102.23.45.67', 'DEVICE888', NOW(), NOW()),
(8, 3, 7500.00, 'USD', 'Diamond Store', 'JEWELRY', 'PURCHASE', 'BLOCKED', 'Beijing, China', '123.45.67.89', 'DEVICE777', NOW(), NOW()),
(9, 4, 3000.00, 'USD', 'Online Casino', 'ONLINE_GAMING', 'PURCHASE', 'FLAGGED', 'Manila, Philippines', '156.78.90.12', 'DEVICE666', NOW(), NOW()),
(10, 1, 500.00, 'USD', 'Electronics Store', 'ELECTRONICS', 'PURCHASE', 'COMPLETED', 'Tokyo, Japan', '78.90.12.34', 'DEVICE001', NOW(), NOW());

-- Insert fraud alerts
INSERT INTO fraud_alerts (id, transaction_id, user_id, alert_type, risk_score, fraud_indicators, status, detected_at, resolved_at, notes) 
VALUES 
(1, 6, 1, 'HIGH_VALUE_TRANSACTION', 95.5, 'Amount exceeds normal pattern by 200x, Unusual location (Russia), High-risk merchant category', 'OPEN', NOW(), NULL, 'User normally spends $50-$200. This transaction is $9999.99'),
(2, 7, 2, 'SUSPICIOUS_LOCATION', 88.0, 'Transaction from high-risk country, Cryptocurrency purchase, New device', 'UNDER_INVESTIGATION', NOW(), NULL, 'User account shows login from Nigeria - possible account takeover'),
(3, 8, 3, 'MERCHANT_RISK', 92.3, 'High-risk merchant category, Unusual location (China), Amount 37x above normal', 'CONFIRMED_FRAUD', NOW(), NOW(), 'User confirmed unauthorized transaction. Card blocked.'),
(4, 9, 4, 'ONLINE_GAMING', 85.0, 'Online gaming transaction, High amount, New IP address', 'OPEN', NOW(), NULL, 'Monitoring for additional suspicious activity'),
(5, 10, 1, 'VELOCITY_CHECK', 78.5, 'Rapid location change (NY to Tokyo in 2 hours), Same user ID', 'FALSE_POSITIVE', NOW(), NOW(), 'User traveled via flight - legitimate transaction');

-- Insert blockchain blocks (for fraud records)
INSERT INTO fraud_blocks (id, block_index, previous_hash, current_hash, transaction_data, timestamp, nonce, mined_by, difficulty) 
VALUES 
(1, 0, '0', 'genesis_hash_000000', 'Genesis Block - Fraud Detection System Initialized', NOW(), 0, 'SYSTEM', 4),
(2, 1, 'genesis_hash_000000', 'block1_hash_0000ab', 'Alert #1: High value transaction detected for user 1', NOW(), 12345, 'FRAUD_ENGINE', 4),
(3, 2, 'block1_hash_0000ab', 'block2_hash_0000cd', 'Alert #2: Suspicious location detected for user 2', NOW(), 23456, 'FRAUD_ENGINE', 4),
(4, 3, 'block2_hash_0000cd', 'block3_hash_0000ef', 'Alert #3: Confirmed fraud for transaction 8', NOW(), 34567, 'FRAUD_ENGINE', 4);

-- Insert smart contracts
INSERT INTO smart_contracts (id, contract_name, contract_type, trigger_condition, action_type, status, created_at, executed_at, executed_by) 
VALUES 
(1, 'Auto-Block High Risk', 'FRAUD_PREVENTION', 'RISK_SCORE > 90', 'BLOCK_TRANSACTION', 'ACTIVE', NOW(), NULL, NULL),
(2, 'Freeze Suspicious Accounts', 'ACCOUNT_PROTECTION', 'MULTIPLE_FAILED_ATTEMPTS > 3', 'FREEZE_ACCOUNT', 'ACTIVE', NOW(), NULL, NULL),
(3, 'Alert Admin on High Value', 'NOTIFICATION', 'AMOUNT > 5000', 'SEND_ALERT', 'ACTIVE', NOW(), NOW(), 'SYSTEM'),
(4, 'Geographic Anomaly Action', 'LOCATION_BASED', 'LOCATION_CHANGE_VELOCITY > 500mph', 'REQUEST_VERIFICATION', 'ACTIVE', NOW(), NULL, NULL),
(5, 'Crypto Transaction Monitor', 'MERCHANT_MONITORING', 'MERCHANT_CATEGORY = CRYPTOCURRENCY', 'ENHANCED_MONITORING', 'ACTIVE', NOW(), NOW(), 'SYSTEM');

-- Additional normal transactions to build user history
INSERT INTO transactions (user_id, amount, currency, merchant_name, merchant_category, transaction_type, status, location, ip_address, device_id, timestamp, created_at) 
VALUES 
(1, 35.20, 'USD', 'Target', 'GROCERY', 'PURCHASE', 'COMPLETED', 'New York, USA', '192.168.1.1', 'DEVICE001', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 78.45, 'USD', 'Home Depot', 'ELECTRONICS', 'PURCHASE', 'COMPLETED', 'New York, USA', '192.168.1.1', 'DEVICE001', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 15.99, 'USD', 'McDonalds', 'RESTAURANT', 'PURCHASE', 'COMPLETED', 'Los Angeles, USA', '192.168.2.1', 'DEVICE002', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 250.00, 'USD', 'Apple Store', 'ELECTRONICS', 'PURCHASE', 'COMPLETED', 'Chicago, USA', '192.168.3.1', 'DEVICE003', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 50.00, 'USD', 'Uber', 'TRAVEL', 'PURCHASE', 'COMPLETED', 'Miami, USA', '192.168.4.1', 'DEVICE004', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));