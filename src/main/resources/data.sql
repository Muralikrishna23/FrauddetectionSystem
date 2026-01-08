-- ============================================
-- COMPLETE DATABASE INSERT SCRIPT
-- MySQL Database: fraud_detection
-- ============================================

USE fraud_detection;

-- Clear existing data (optional - use with caution)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE fraud_alerts;
TRUNCATE TABLE fraud_blocks;
TRUNCATE TABLE smart_contracts;
TRUNCATE TABLE transactions;
TRUNCATE TABLE user_profiles;
TRUNCATE TABLE users;
TRUNCATE TABLE merchant_categories;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- TABLE 1: USERS (20 users)
-- ============================================
INSERT INTO users (user_id, email, first_name, last_name, phone_number, created_at, updated_at, is_active) VALUES
('user001', 'john.doe@email.com', 'John', 'Doe', '+1-555-0101', NOW() - INTERVAL 365 DAY, NOW(), TRUE),
('user002', 'jane.smith@email.com', 'Jane', 'Smith', '+1-555-0102', NOW() - INTERVAL 320 DAY, NOW(), TRUE),
('user003', 'bob.wilson@email.com', 'Bob', 'Wilson', '+1-555-0103', NOW() - INTERVAL 280 DAY, NOW(), TRUE),
('user004', 'alice.brown@email.com', 'Alice', 'Brown', '+1-555-0104', NOW() - INTERVAL 250 DAY, NOW(), TRUE),
('user005', 'charlie.davis@email.com', 'Charlie', 'Davis', '+1-555-0105', NOW() - INTERVAL 200 DAY, NOW(), FALSE),
('user006', 'diana.miller@email.com', 'Diana', 'Miller', '+1-555-0106', NOW() - INTERVAL 180 DAY, NOW(), TRUE),
('user007', 'edward.garcia@email.com', 'Edward', 'Garcia', '+1-555-0107', NOW() - INTERVAL 150 DAY, NOW(), TRUE),
('user008', 'fiona.martinez@email.com', 'Fiona', 'Martinez', '+1-555-0108', NOW() - INTERVAL 120 DAY, NOW(), TRUE),
('user009', 'george.rodriguez@email.com', 'George', 'Rodriguez', '+1-555-0109', NOW() - INTERVAL 90 DAY, NOW(), TRUE),
('user010', 'hannah.lopez@email.com', 'Hannah', 'Lopez', '+1-555-0110', NOW() - INTERVAL 60 DAY, NOW(), TRUE),
('user011', 'ivan.gonzalez@email.com', 'Ivan', 'Gonzalez', '+1-555-0111', NOW() - INTERVAL 50 DAY, NOW(), TRUE),
('user012', 'julia.hernandez@email.com', 'Julia', 'Hernandez', '+1-555-0112', NOW() - INTERVAL 40 DAY, NOW(), TRUE),
('user013', 'kevin.moore@email.com', 'Kevin', 'Moore', '+1-555-0113', NOW() - INTERVAL 30 DAY, NOW(), TRUE),
('user014', 'laura.taylor@email.com', 'Laura', 'Taylor', '+1-555-0114', NOW() - INTERVAL 25 DAY, NOW(), FALSE),
('user015', 'michael.anderson@email.com', 'Michael', 'Anderson', '+1-555-0115', NOW() - INTERVAL 20 DAY, NOW(), TRUE),
('user016', 'nancy.thomas@email.com', 'Nancy', 'Thomas', '+1-555-0116', NOW() - INTERVAL 15 DAY, NOW(), TRUE),
('user017', 'oliver.jackson@email.com', 'Oliver', 'Jackson', '+1-555-0117', NOW() - INTERVAL 10 DAY, NOW(), TRUE),
('user018', 'patricia.white@email.com', 'Patricia', 'White', '+1-555-0118', NOW() - INTERVAL 8 DAY, NOW(), TRUE),
('user019', 'quinn.harris@email.com', 'Quinn', 'Harris', '+1-555-0119', NOW() - INTERVAL 5 DAY, NOW(), TRUE),
('user020', 'rachel.martin@email.com', 'Rachel', 'Martin', '+1-555-0120', NOW() - INTERVAL 2 DAY, NOW(), TRUE);

-- ============================================
-- TABLE 2: USER PROFILES
-- ============================================
INSERT INTO user_profiles (user_id, average_monthly_spending, typical_spending_locations, preferred_payment_methods, risk_score, occupation, income_range) VALUES
(1, 2500.00, 'New York,Los Angeles', 'CREDIT_CARD,DEBIT_CARD', 0.15, 'Software Engineer', '$80,000-$100,000'),
(2, 3200.00, 'Los Angeles,San Francisco', 'CREDIT_CARD,DIGITAL_WALLET', 0.10, 'Marketing Manager', '$90,000-$120,000'),
(3, 1800.00, 'Chicago,Detroit', 'DEBIT_CARD,BANK_TRANSFER', 0.25, 'Teacher', '$50,000-$70,000'),
(4, 4500.00, 'Miami,Orlando', 'CREDIT_CARD', 0.05, 'Doctor', '$150,000-$200,000'),
(5, 1200.00, 'Dallas,Houston', 'CASH,DEBIT_CARD', 0.85, 'Retail Worker', '$30,000-$40,000'),
(6, 2800.00, 'Seattle,Portland', 'CREDIT_CARD,DIGITAL_WALLET', 0.12, 'Nurse', '$70,000-$90,000'),
(7, 5500.00, 'Boston,New York', 'CREDIT_CARD', 0.08, 'Lawyer', '$180,000-$250,000'),
(8, 2100.00, 'Phoenix,Tucson', 'DEBIT_CARD,CREDIT_CARD', 0.18, 'Accountant', '$60,000-$80,000'),
(9, 3800.00, 'San Diego,Los Angeles', 'CREDIT_CARD,DIGITAL_WALLET', 0.11, 'Consultant', '$100,000-$130,000'),
(10, 1500.00, 'Denver,Boulder', 'DEBIT_CARD', 0.22, 'Student', '$20,000-$30,000'),
(11, 4200.00, 'Austin,San Antonio', 'CREDIT_CARD', 0.09, 'Business Owner', '$120,000-$160,000'),
(12, 2600.00, 'Atlanta,Savannah', 'CREDIT_CARD,BANK_TRANSFER', 0.14, 'Real Estate Agent', '$75,000-$95,000'),
(13, 1900.00, 'Nashville,Memphis', 'DEBIT_CARD,CREDIT_CARD', 0.20, 'Musician', '$45,000-$60,000'),
(14, 3100.00, 'Charlotte,Raleigh', 'CREDIT_CARD', 0.72, 'Sales Manager', '$85,000-$110,000'),
(15, 5200.00, 'Washington DC,Baltimore', 'CREDIT_CARD,DIGITAL_WALLET', 0.06, 'Government Official', '$140,000-$180,000'),
(16, 2400.00, 'Minneapolis,St Paul', 'DEBIT_CARD,CREDIT_CARD', 0.16, 'Engineer', '$70,000-$90,000'),
(17, 1700.00, 'Cleveland,Columbus', 'CASH,DEBIT_CARD', 0.28, 'Factory Worker', '$40,000-$55,000'),
(18, 3600.00, 'Las Vegas,Reno', 'CREDIT_CARD', 0.13, 'Hotel Manager', '$95,000-$125,000'),
(19, 2900.00, 'Philadelphia,Pittsburgh', 'CREDIT_CARD,BANK_TRANSFER', 0.10, 'Pharmacist', '$90,000-$115,000'),
(20, 4800.00, 'San Francisco,San Jose', 'DIGITAL_WALLET,CREDIT_CARD', 0.07, 'Tech Executive', '$160,000-$220,000');

-- ============================================
-- TABLE 3: MERCHANT CATEGORIES
-- ============================================
INSERT INTO merchant_categories (category_code, category_name, description, risk_level) VALUES
('GRO', 'Grocery', 'Supermarkets and grocery stores', 'LOW'),
('ELE', 'Electronics', 'Electronics and technology retailers', 'MEDIUM'),
('JWL', 'Jewelry', 'Jewelry and precious metals', 'HIGH'),
('GAM', 'Online Gaming', 'Online gaming and gambling', 'HIGH'),
('RES', 'Restaurant', 'Restaurants and dining', 'LOW'),
('GAS', 'Gas Station', 'Fuel and gas stations', 'LOW'),
('CRY', 'Cryptocurrency', 'Cryptocurrency exchanges', 'CRITICAL'),
('TRV', 'Travel', 'Travel agencies and booking', 'MEDIUM'),
('CLO', 'Clothing', 'Apparel and fashion stores', 'LOW'),
('HOM', 'Home Improvement', 'Hardware and home improvement', 'LOW'),
('HEA', 'Healthcare', 'Medical services and pharmacies', 'MEDIUM'),
('EDU', 'Education', 'Educational services and supplies', 'LOW'),
('ENT', 'Entertainment', 'Movies, concerts, events', 'LOW'),
('WIR', 'Wire Transfer', 'Money transfer services', 'CRITICAL'),
('PAW', 'Pawn Shop', 'Pawn shops and second-hand dealers', 'HIGH'),
('CHK', 'Check Cashing', 'Check cashing services', 'HIGH'),
('LOT', 'Lottery', 'Lottery and betting', 'HIGH'),
('AUT', 'Automotive', 'Car dealerships and services', 'MEDIUM'),
('INS', 'Insurance', 'Insurance services', 'LOW'),
('UTI', 'Utilities', 'Utility bill payments', 'LOW');

-- ============================================
-- TABLE 4: TRANSACTIONS (50+ transactions)
-- ============================================

-- Normal transactions for various users
INSERT INTO transactions (transaction_id, user_id, amount, merchant_category_id, merchant_name, location, timestamp, payment_method, is_fraudulent, fraud_score, processing_status, description) VALUES
-- User 1 - Normal spending pattern
('TXN001', 1, 45.99, 1, 'Walmart Supercenter', 'New York, USA', NOW() - INTERVAL 30 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Weekly groceries'),
('TXN002', 1, 89.50, 2, 'Best Buy', 'New York, USA', NOW() - INTERVAL 28 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'USB drive purchase'),
('TXN003', 1, 125.00, 5, 'Olive Garden', 'New York, USA', NOW() - INTERVAL 25 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Dinner with family'),
('TXN004', 1, 52.30, 1, 'Trader Joes', 'New York, USA', NOW() - INTERVAL 20 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Groceries'),
('TXN005', 1, 35.75, 6, 'Shell Gas Station', 'New York, USA', NOW() - INTERVAL 15 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Fuel'),

-- User 2 - Normal pattern
('TXN006', 2, 25.00, 5, 'Starbucks', 'Los Angeles, USA', NOW() - INTERVAL 27 DAY, 'DIGITAL_WALLET', FALSE, 0.00, 'APPROVED', 'Coffee'),
('TXN007', 2, 120.75, 9, 'Zara', 'Los Angeles, USA', NOW() - INTERVAL 24 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Clothing'),
('TXN008', 2, 89.99, 13, 'AMC Theatres', 'Los Angeles, USA', NOW() - INTERVAL 18 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Movie tickets'),
('TXN009', 2, 210.50, 1, 'Whole Foods', 'Los Angeles, USA', NOW() - INTERVAL 12 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Organic groceries'),

-- User 3 - Normal pattern
('TXN010', 3, 199.99, 2, 'Amazon', 'Chicago, USA', NOW() - INTERVAL 22 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Laptop accessories'),
('TXN011', 3, 67.45, 1, 'Target', 'Chicago, USA', NOW() - INTERVAL 16 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Household items'),
('TXN012', 3, 145.00, 10, 'Home Depot', 'Chicago, USA', NOW() - INTERVAL 10 DAY, 'BANK_TRANSFER', FALSE, 0.00, 'APPROVED', 'Paint supplies'),

-- User 4 - High income, larger transactions
('TXN013', 4, 550.00, 5, 'The Capital Grille', 'Miami, USA', NOW() - INTERVAL 21 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Business dinner'),
('TXN014', 4, 1250.00, 2, 'Apple Store', 'Miami, USA', NOW() - INTERVAL 14 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'iPad Pro'),
('TXN015', 4, 890.00, 8, 'Expedia', 'Miami, USA', NOW() - INTERVAL 8 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Flight booking'),

-- FRAUDULENT TRANSACTIONS - High value, suspicious locations
('TXN016', 1, 9999.99, 3, 'Luxury Jewelers Moscow', 'Moscow, Russia', NOW() - INTERVAL 5 DAY, 'CREDIT_CARD', TRUE, 0.95, 'UNDER_REVIEW', 'Suspicious high-value jewelry'),
('TXN017', 2, 5000.00, 7, 'CryptoMax Exchange', 'Lagos, Nigeria', NOW() - INTERVAL 4 DAY, 'CREDIT_CARD', TRUE, 0.88, 'UNDER_REVIEW', 'Crypto purchase from high-risk location'),
('TXN018', 3, 7500.00, 3, 'Diamond Palace', 'Beijing, China', NOW() - INTERVAL 3 DAY, 'DEBIT_CARD', TRUE, 0.92, 'DECLINED', 'Unauthorized diamond purchase'),
('TXN019', 4, 3000.00, 4, 'Royal Online Casino', 'Manila, Philippines', NOW() - INTERVAL 2 DAY, 'CREDIT_CARD', TRUE, 0.85, 'UNDER_REVIEW', 'Online gambling'),
('TXN020', 5, 8500.00, 14, 'FastWire Transfer', 'Bangkok, Thailand', NOW() - INTERVAL 1 DAY, 'BANK_TRANSFER', TRUE, 0.93, 'DECLINED', 'Suspicious wire transfer'),

-- More normal transactions
('TXN021', 6, 175.50, 11, 'CVS Pharmacy', 'Seattle, USA', NOW() - INTERVAL 19 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Prescriptions'),
('TXN022', 7, 2100.00, 8, 'Delta Airlines', 'Boston, USA', NOW() - INTERVAL 17 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Round trip tickets'),
('TXN023', 8, 88.25, 5, 'Chipotle', 'Phoenix, USA', NOW() - INTERVAL 13 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Lunch'),
('TXN024', 9, 450.00, 12, 'Coursera', 'San Diego, USA', NOW() - INTERVAL 11 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Online courses'),
('TXN025', 10, 95.00, 9, 'H&M', 'Denver, USA', NOW() - INTERVAL 9 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Clothes shopping'),

-- Additional fraudulent patterns - Velocity attacks
('TXN026', 11, 500.00, 2, 'Electronics Hub', 'Austin, USA', NOW() - INTERVAL 1 HOUR, 'CREDIT_CARD', FALSE, 0.42, 'APPROVED', 'Tablet purchase'),
('TXN027', 11, 600.00, 2, 'Tech World', 'Houston, USA', NOW() - INTERVAL 30 MINUTE, 'CREDIT_CARD', TRUE, 0.78, 'UNDER_REVIEW', 'Rapid location change'),
('TXN028', 11, 700.00, 2, 'Gadget Store', 'Dallas, USA', NOW() - INTERVAL 15 MINUTE, 'CREDIT_CARD', TRUE, 0.85, 'DECLINED', 'Multiple rapid transactions'),
('TXN029', 11, 800.00, 2, 'Electronics Plus', 'San Antonio, USA', NOW() - INTERVAL 5 MINUTE, 'CREDIT_CARD', TRUE, 0.92, 'DECLINED', 'Velocity fraud detected'),

-- Merchant risk transactions
('TXN030', 12, 1500.00, 15, 'Quick Pawn Shop', 'Atlanta, USA', NOW() - INTERVAL 6 DAY, 'CASH', TRUE, 0.71, 'UNDER_REVIEW', 'High-risk merchant'),
('TXN031', 13, 2500.00, 16, 'EZ Check Cash', 'Nashville, USA', NOW() - INTERVAL 5 DAY, 'CHECK', TRUE, 0.76, 'UNDER_REVIEW', 'Check cashing service'),
('TXN032', 14, 3200.00, 7, 'BitCoin ATM', 'Charlotte, USA', NOW() - INTERVAL 4 DAY, 'CASH', TRUE, 0.89, 'DECLINED', 'Crypto ATM withdrawal'),

-- Normal transactions for newer users
('TXN033', 15, 650.00, 20, 'Electric Company', 'Washington DC, USA', NOW() - INTERVAL 7 DAY, 'BANK_TRANSFER', FALSE, 0.00, 'APPROVED', 'Utility bill'),
('TXN034', 16, 125.50, 1, 'Costco', 'Minneapolis, USA', NOW() - INTERVAL 6 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Bulk groceries'),
('TXN035', 17, 45.00, 6, 'BP Gas Station', 'Cleveland, USA', NOW() - INTERVAL 5 DAY, 'CASH', FALSE, 0.00, 'APPROVED', 'Fuel'),
('TXN036', 18, 890.00, 8, 'Marriott Hotels', 'Las Vegas, USA', NOW() - INTERVAL 4 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Hotel booking'),
('TXN037', 19, 220.00, 11, 'Walgreens', 'Philadelphia, USA', NOW() - INTERVAL 3 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Medical supplies'),
('TXN038', 20, 1850.00, 2, 'Microsoft Store', 'San Francisco, USA', NOW() - INTERVAL 2 DAY, 'DIGITAL_WALLET', FALSE, 0.00, 'APPROVED', 'Surface laptop'),

-- Late night suspicious transactions
('TXN039', 6, 4500.00, 3, 'Gold & Silver Exchange', 'Seattle, USA', NOW() - INTERVAL 1 DAY + INTERVAL 3 HOUR, 'CREDIT_CARD', TRUE, 0.82, 'UNDER_REVIEW', 'Late night jewelry purchase'),
('TXN040', 7, 6700.00, 7, 'Crypto24 Exchange', 'Boston, USA', NOW() - INTERVAL 1 DAY + INTERVAL 2 HOUR, 'BANK_TRANSFER', TRUE, 0.87, 'DECLINED', 'Late night crypto trade'),

-- More normal recent transactions
('TXN041', 1, 78.90, 1, 'Safeway', 'New York, USA', NOW() - INTERVAL 2 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Weekly shopping'),
('TXN042', 2, 155.00, 13, 'Broadway Theater', 'Los Angeles, USA', NOW() - INTERVAL 1 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Theater tickets'),
('TXN043', 3, 92.50, 5, 'Panera Bread', 'Chicago, USA', NOW() - INTERVAL 1 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Lunch meeting'),
('TXN044', 4, 320.00, 18, 'Tesla Service Center', 'Miami, USA', NOW() - INTERVAL 12 HOUR, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Car maintenance'),
('TXN045', 8, 67.30, 1, 'Fry's Food', 'Phoenix, USA', NOW() - INTERVAL 8 HOUR, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Groceries'),

-- Additional fraudulent transactions - Amount-based
('TXN046', 9, 15000.00, 3, 'Diamond Emporium', 'San Diego, USA', NOW() - INTERVAL 4 HOUR, 'CREDIT_CARD', TRUE, 0.96, 'DECLINED', 'Amount 5x above average'),
('TXN047', 10, 12500.00, 2, 'Luxury Electronics', 'Denver, USA', NOW() - INTERVAL 3 HOUR, 'DEBIT_CARD', TRUE, 0.94, 'DECLINED', 'Exceeds student budget'),

-- Recent normal activity
('TXN048', 15, 210.00, 5, 'Ruth's Chris Steakhouse', 'Washington DC, USA', NOW() - INTERVAL 2 HOUR, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Dinner'),
('TXN049', 16, 89.75, 9, 'Gap', 'Minneapolis, USA', NOW() - INTERVAL 1 HOUR, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Clothing'),
('TXN050', 17, 42.50, 6, 'Chevron', 'Cleveland, USA', NOW() - INTERVAL 30 MINUTE, 'CASH', FALSE, 0.00, 'APPROVED', 'Gas');

-- ============================================
-- TABLE 5: FRAUD ALERTS
-- ============================================
INSERT INTO fraud_alerts (user_id, transaction_id, alert_time, alert_type, description, triggered_rules, severity, confidence_score, status, resolved_at, resolved_by) VALUES
(1, 16, NOW() - INTERVAL 5 DAY, 'HIGH_VALUE_TRANSACTION', 'Transaction amount $9999.99 is 200x above user average. Unusual location: Moscow, Russia. High-risk merchant category.', 'Amount-Based Rule, Merchant Risk Rule', 'CRITICAL', 0.95, 'OPEN', NULL, NULL),
(2, 17, NOW() - INTERVAL 4 DAY, 'SUSPICIOUS_LOCATION', 'Transaction from high-risk country (Nigeria). Cryptocurrency purchase. New device detected.', 'Merchant Risk Rule', 'HIGH', 0.88, 'UNDER_INVESTIGATION', NULL, NULL),
(3, 18, NOW() - INTERVAL 3 DAY, 'MERCHANT_RISK', 'High-risk merchant category. Unusual location (China). Amount 37x above normal pattern.', 'Amount-Based Rule, Merchant Risk Rule', 'CRITICAL', 0.92, 'RESOLVED', NOW() - INTERVAL 2 DAY, 'admin'),
(4, 19, NOW() - INTERVAL 2 DAY, 'ONLINE_GAMING', 'Online gaming transaction from Philippines. Amount exceeds typical spending.', 'Merchant Risk Rule', 'HIGH', 0.85, 'OPEN', NULL, NULL),
(5, 20, NOW() - INTERVAL 1 DAY, 'WIRE_TRANSFER', 'Wire transfer from high-risk location. User account shows abnormal activity.', 'Merchant Risk Rule', 'CRITICAL', 0.93, 'OPEN', NULL, NULL),
(11, 27, NOW() - INTERVAL 30 MINUTE, 'VELOCITY_CHECK', 'Rapid location change detected. 4 transactions in 1 hour across different cities.', 'Frequency-Based Rule', 'HIGH', 0.78, 'OPEN', NULL, NULL),
(11, 28, NOW() - INTERVAL 15 MINUTE, 'RAPID_TRANSACTIONS', 'Multiple rapid transactions detected in electronics category.', 'Frequency-Based Rule, Amount-Based Rule', 'HIGH', 0.85, 'OPEN', NULL, NULL),
(11, 29, NOW() - INTERVAL 5 MINUTE, 'VELOCITY_FRAUD', 'Severe velocity fraud pattern. 4 transactions in different cities within 1 hour.', 'Frequency-Based Rule', 'CRITICAL', 0.92, 'OPEN', NULL, NULL),
(12, 30, NOW() - INTERVAL 6 DAY, 'HIGH_RISK_MERCHANT', 'Transaction at pawn shop with high amount. Elevated fraud indicators.', 'Merchant Risk Rule', 'MEDIUM', 0.71, 'UNDER_INVESTIGATION', NULL, NULL),
(13, 31, NOW() - INTERVAL 5 DAY, 'CHECK_CASHING', 'Check cashing service transaction. High amount for user profile.', 'Merchant Risk Rule', 'HIGH', 0.76, 'UNDER_INVESTIGATION', NULL, NULL),
(14, 32, NOW() - INTERVAL 4 DAY, 'CRYPTOCURRENCY', 'Bitcoin ATM withdrawal. Previously suspended account activity.', 'Merchant Risk Rule', 'CRITICAL', 0.89, 'OPEN', NULL, NULL),
(6, 39, NOW() - INTERVAL 1 DAY + INTERVAL 3 HOUR, 'LATE_NIGHT_ACTIVITY', 'High-value jewelry purchase at 3 AM. Unusual time for user.', 'Merchant Risk Rule', 'HIGH', 0.82, 'OPEN', NULL, NULL),
(7, 40, NOW() - INTERVAL 1 DAY + INTERVAL 2 HOUR, 'LATE_NIGHT_CRYPTO', 'Cryptocurrency transaction at 2 AM. Large amount.', 'Merchant Risk Rule', 'CRITICAL', 0.87, 'OPEN', NULL, NULL),
(9, 46, NOW() - INTERVAL 4 HOUR, 'AMOUNT_ANOMALY', 'Transaction amount $15,000 is 5x above user average spending pattern.', 'Amount-Based Rule', 'CRITICAL', 0.96, 'OPEN', NULL, NULL),
(10, 47, NOW() - INTERVAL 3 HOUR, 'STUDENT_LARGE_PURCHASE', 'Student account with $12,500 transaction. Far exceeds typical student budget.', 'Amount-Based Rule', 'CRITICAL', 0.94, 'OPEN', NULL, NULL);

-- ============================================
-- TABLE 6: FRAUD BLOCKS (BLOCKCHAIN)
-- ============================================
INSERT INTO fraud_blocks (block_index, timestamp, transaction_data, previous_hash, current_hash, nonce, fraud_decision, transaction_id, risk_score, fraud_reasons, validator_signature, is_valid) VALUES
(0, NOW() - INTERVAL 30 DAY, '{"type":"genesis","message":"Fraud Detection System Initialized"}', '0', '0000000000genesis1234567890abcdef', 0, 'SYSTEM_INIT', 'GENESIS', 0.00, 'Genesis Block', 'SYSTEM', TRUE),
(1, NOW() - INTERVAL 5 DAY, '{"txnId":"TXN016","userId":"user001","amount":9999.99,"location":"Moscow, Russia"}', '0000000000genesis1234567890abcdef', '0000a1b2c3d4e5f6789012345678901', 12345, 'FLAGGED', 'TXN016', 0.95, 'High value, suspicious location, high-risk merchant', 'VALIDATOR_A1', TRUE),
(2, NOW() - INTERVAL 4 DAY, '{"txnId":"TXN017","userId":"user002","amount":5000.00,"location":"Lagos, Nigeria"}', '0000a1b2c3d4e5f6789012345678901', '0000b2c3d4e5f67890123456789012', 23456, 'FLAGGED', 'TXN017', 0.88, 'Cryptocurrency from high-risk country', 'VALIDATOR_B2', TRUE),
(3, NOW() - INTERVAL 3 DAY, '{"txnId":"TXN018","userId":"user003","amount":7500.00,"location":"Beijing, China"}', '0000b2c3d4e5f67890123456789012', '0000c3d4e5f678901234567890123', 34567, 'BLOCKED', 'TXN018', 0.92, 'Confirmed unauthorized transaction', 'VALIDATOR_C3', TRUE),
(4, NOW() - INTERVAL 2 DAY, '{"txnId":"TXN019","userId":"user004","amount":3000.00,"location":"Manila, Philippines"}', '0000c3d4e5f678901234567890123', '0000d4e5f6789012345678901234', 45678, 'FLAGGED', 'TXN019', 0.85, 'Online gambling from foreign location', 'VALIDATOR_D4', TRUE),
(5, NOW() - INTERVAL 1 DAY, '{"txnId":"TXN020","userId":"user005","amount":8500.00,"location":"Bangkok, Thailand"}', '0000d4e5f6789012345678901234', '0000e5f67890123456789012345', 56789, 'BLOCKED', 'TXN020', 0.93, 'Suspicious wire transfer, inactive user account', 'VALIDATOR_E5', TRUE),
(6, NOW() - INTERVAL 30 MINUTE, '{"txnId":"TXN027","userId":"user011","amount":600.00,"location":"Houston, USA"}', '0000e5f67890123456789012345', '0000f678901234567890123456', 67890, 'FLAGGED', 'TXN027', 0.78, 'Rapid location change detected', 'VALIDATOR_F6', TRUE),
(7, NOW() - INTERVAL 15 MINUTE, '{"txnId":"TXN028","userId":"user011","amount":700.00,"location":"Dallas, USA"}', '0000f678901234567890123456', '000067890123456789012345678', 78901, 'BLOCKED', 'TXN028', 0.85, 'Multiple rapid transactions', 'VALIDATOR_G7', TRUE),
(8, NOW() - INTERVAL 5 MINUTE, '{"txnId":"TXN029","userId":"user011","amount":800.00,"location":"San Antonio, USA"}', '000067890123456789012345678', '000178901234567890123456789', 89012, 'BLOCKED', 'TXN029', 0.92, 'Velocity fraud pattern confirmed', 'VALIDATOR_H8', TRUE),
(9, NOW() - INTERVAL 6 DAY, '{"txnId":"TXN030","userId":"user012","amount":1500.00,"location":"Atlanta, USA"}', '000178901234567890123456789', '000289012345678901234567890', 90123, 'FLAGGED', 'TXN030', 0.71, 'High-risk pawn shop merchant', 'VALIDATOR_I9', TRUE),
(10, NOW() - INTERVAL 5 DAY, '{"txnId":"TXN031","userId":"user013","amount":2500.00,"location":"Nashville, USA"}', '000289012345678901234567890', '00039012345678901234567890a', 101234, 'FLAGGED', 'TXN031', 0.76, 'Check cashing service alert', 'VALIDATOR_J10', TRUE),
(11, NOW() - INTERVAL 4 DAY, '{"txnId":"TXN032","userId":"user014","amount":3200.00,"location":"Charlotte, USA"}', '00039012345678901234567890a', '0004a012345678901234567890b', 112345, 'BLOCKED', 'TXN032', 0.89, 'Crypto ATM on suspended account', 'VALIDATOR_K11', TRUE),
(12, NOW() - INTERVAL 1 DAY + INTERVAL 3 HOUR, '{"txnId":"TXN039","userId":"user006","amount":4500.00,"location":"Seattle, USA"}', '0004a012345678901234567890b', '0005b012345678901234567890c', 123456, 'FLAGGED', 'TXN039', 0.82, 'Late night jewelry purchase', 'VALIDATOR_L12', TRUE),
(13, NOW() - INTERVAL 1 DAY + INTERVAL 2 HOUR, '{"txnId":"TXN040","userId":"user007","amount":6700.00,"location":"Boston, USA"}', '0005b012345678901234567890c', '0006c012345678901234567890d', 134567, 'BLOCKED', 'TXN040', 0.87, 'Late night crypto transaction', 'VALIDATOR_M13', TRUE),
(14, NOW() - INTERVAL 4 HOUR, '{"txnId":"TXN046","userId":"user009","amount":15000.00,"location":"San Diego, USA"}', '0006c012345678901234567890d', '0007d012345678901234567890e', 145678, 'BLOCKED', 'TXN046', 0.96, 'Amount 5x above user average', 'VALIDATOR_N14', TRUE),
(15, NOW() - INTERVAL 3 HOUR, '{"txnId":"TXN047","userId":"user010","amount":12500.00,"location":"Denver, USA"}', '0007d012345678901234567890e', '0008e012345678901234567890f', 156789, 'BLOCKED', 'TXN047', 0.94, 'Student account with excessive amount', 'VALIDATOR_O15', TRUE);

-- ============================================
-- TABLE 7: SMART CONTRACTS (BLOCKCHAIN)
-- ============================================
INSERT INTO smart_contracts (contract_id, contract_name, contract_rules, action_type, risk_threshold, is_active, created_at, last_executed_at, execution_count, description) VALUES
('SC-AUTO001', 'High Risk Auto Block', '{"condition":"riskScore >= 0.90","priority":"critical"}', 'BLOCK_ACCOUNT', 0.90, TRUE, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 3 HOUR, 5, 'Automatically block accounts with risk score >= 0.90'),
('SC-FREEZE01', 'Freeze Suspicious Accounts', '{"condition":"riskScore >= 0.80 && riskScore < 0.90","priority":"high"}', 'FREEZE_FUNDS', 0.80, TRUE, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 4 HOUR, 8, 'Freeze funds for high-confidence suspicious transactions'),
('SC-ALERT01', 'Admin Alert Medium Risk', '{"condition":"riskScore >= 0.50 && riskScore < 0.80","priority":"medium"}', 'ALERT_ADMIN', 0.50, TRUE, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 2 HOUR, 15, 'Send alert to admins for medium-risk transactions'),
('SC-2FA001', '2FA Requirement Trigger', '{"condition":"riskScore >= 0.60","action":"require_verification"}', 'REQUIRE_2FA', 0.60, TRUE, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 1 HOUR, 22, 'Require two-factor authentication for suspicious transactions'),
('SC-CRYPTO01', 'Cryptocurrency Monitor', '{"condition":"merchantCategory = CRYPTOCURRENCY","enhanced":true}', 'ENHANCED_MONITORING', 0.50, TRUE, NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 4 DAY, 12, 'Enhanced monitoring for all cryptocurrency transactions'),
('SC-VELOCITY', 'Velocity Fraud Detector', '{"condition":"transactionCount > 3 && timeWindow < 3600","type":"velocity"}', 'DECLINE_TRANSACTION', 0.70, TRUE, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 30 MINUTE, 4, 'Detect and decline rapid transaction patterns'),
('SC-LOCATION', 'Geographic Anomaly', '{"condition":"locationChange > 500 && timeWindow < 7200","type":"location"}', 'REQUEST_VERIFICATION', 0.65, TRUE, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 1 DAY, 3, 'Request verification for impossible travel patterns'),
('SC-AMOUNT01', 'Large Amount Alert', '{"condition":"amount > 10000","threshold":"high"}', 'ALERT_ADMIN', 0.00, TRUE, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 4 HOUR, 7, 'Alert on any transaction above $10,000'),
('SC-NIGHT01', 'Night Time Monitoring', '{"condition":"hour >= 2 && hour <= 6 && amount > 1000","type":"temporal"}', 'REQUIRE_2FA', 0.55, TRUE, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 1 DAY + INTERVAL 3 HOUR, 2, 'Extra verification for large late-night transactions'),
('SC-PAWN01', 'High Risk Merchant Block', '{"condition":"merchantType IN [PAWN_SHOP, CHECK_CASHING, LOTTERY]","risk":"high"}', 'ENHANCED_MONITORING', 0.60, TRUE, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 6 DAY, 3, 'Monitor transactions at high-risk merchant types'),
('SC-SUSPEND01', 'Suspended Account Monitor', '{"condition":"accountStatus = SUSPENDED","strict":true}', 'BLOCK_ACCOUNT', 0.00, TRUE, NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 4 DAY, 1, 'Block all transactions from suspended accounts'),
('SC-MULTI01', 'Multiple Device Alert', '{"condition":"uniqueDevices > 3 && timeWindow < 86400","type":"device"}', 'ALERT_ADMIN', 0.55, TRUE, NOW() - INTERVAL 10 DAY, NULL, 0, 'Alert when multiple devices used in 24 hours'),
('SC-FOREIGN01', 'Foreign Transaction Monitor', '{"condition":"country IN [RU, NG, CN, PH] && amount > 1000","risk":"location"}', 'REQUIRE_2FA', 0.65, TRUE, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 5 DAY, 6, 'Enhanced verification for transactions from high-risk countries'),
('SC-WIRE01', 'Wire Transfer Control', '{"condition":"paymentMethod = WIRE_TRANSFER && amount > 2000","critical":true}', 'ALERT_ADMIN', 0.70, TRUE, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 1 DAY, 1, 'Alert on large wire transfers'),
('SC-STUDENT01', 'Student Account Limit', '{"condition":"occupation = Student && amount > 5000","type":"profile"}', 'DECLINE_TRANSACTION', 0.75, TRUE, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 3 HOUR, 1, 'Decline unusually large transactions for student accounts');

-- ============================================
-- ADDITIONAL DATA - Historical Transactions
-- ============================================

-- Add more historical transactions for better patterns (Last 30-90 days)
INSERT INTO transactions (transaction_id, user_id, amount, merchant_category_id, merchant_name, location, timestamp, payment_method, is_fraudulent, fraud_score, processing_status, description) VALUES
-- User 1 historical
('TXN051', 1, 52.30, 1, 'Stop & Shop', 'New York, USA', NOW() - INTERVAL 60 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Groceries'),
('TXN052', 1, 38.90, 6, 'Mobil', 'New York, USA', NOW() - INTERVAL 58 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Fuel'),
('TXN053', 1, 125.00, 2, 'Best Buy', 'New York, USA', NOW() - INTERVAL 55 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Headphones'),
('TXN054', 1, 67.50, 5, 'Applebees', 'New York, USA', NOW() - INTERVAL 50 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Dinner'),
('TXN055', 1, 43.20, 1, 'Whole Foods', 'New York, USA', NOW() - INTERVAL 45 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Organic food'),

-- User 2 historical
('TXN056', 2, 89.99, 9, 'Nordstrom', 'Los Angeles, USA', NOW() - INTERVAL 65 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Shoes'),
('TXN057', 2, 42.50, 5, 'The Cheesecake Factory', 'Los Angeles, USA', NOW() - INTERVAL 62 DAY, 'DIGITAL_WALLET', FALSE, 0.00, 'APPROVED', 'Lunch'),
('TXN058', 2, 156.00, 13, 'Universal Studios', 'Los Angeles, USA', NOW() - INTERVAL 58 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Theme park'),
('TXN059', 2, 78.30, 1, 'Trader Joes', 'Los Angeles, USA', NOW() - INTERVAL 53 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Weekly shopping'),

-- User 3 historical
('TXN060', 3, 210.00, 10, 'Lowes', 'Chicago, USA', NOW() - INTERVAL 70 DAY, 'BANK_TRANSFER', FALSE, 0.00, 'APPROVED', 'Home repair'),
('TXN061', 3, 89.50, 1, 'Jewel-Osco', 'Chicago, USA', NOW() - INTERVAL 67 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Groceries'),
('TXN062', 3, 45.75, 6, 'Marathon', 'Chicago, USA', NOW() - INTERVAL 63 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Gas'),
('TXN063', 3, 125.00, 9, 'Macys', 'Chicago, USA', NOW() - INTERVAL 59 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Clothing'),

-- User 4 historical (high spender)
('TXN064', 4, 850.00, 5, 'Flemings Steakhouse', 'Miami, USA', NOW() - INTERVAL 68 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Business dinner'),
('TXN065', 4, 1450.00, 8, 'Four Seasons', 'Miami, USA', NOW() - INTERVAL 64 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Hotel stay'),
('TXN066', 4, 650.00, 2, 'Best Buy', 'Miami, USA', NOW() - INTERVAL 60 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Smart TV'),
('TXN067', 4, 2100.00, 8, 'American Airlines', 'Miami, USA', NOW() - INTERVAL 56 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'First class tickets'),

-- User 6-10 historical
('TXN068', 6, 95.50, 11, 'Rite Aid', 'Seattle, USA', NOW() - INTERVAL 72 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Pharmacy'),
('TXN069', 7, 1850.00, 18, 'Lexus Service', 'Boston, USA', NOW() - INTERVAL 69 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Car service'),
('TXN070', 8, 67.80, 1, 'Albertsons', 'Phoenix, USA', NOW() - INTERVAL 66 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Groceries'),
('TXN071', 9, 345.00, 12, 'LinkedIn Learning', 'San Diego, USA', NOW() - INTERVAL 63 DAY, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Professional courses'),
('TXN072', 10, 78.90, 9, 'Forever 21', 'Denver, USA', NOW() - INTERVAL 60 DAY, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Casual wear'),

-- Today's transactions (very recent)
('TXN073', 1, 55.30, 1, 'Wegmans', 'New York, USA', NOW() - INTERVAL 5 HOUR, 'CREDIT_CARD', FALSE, 0.00, 'APPROVED', 'Fresh groceries'),
('TXN074', 2, 32.50, 5, 'Chipotle', 'Los Angeles, USA', NOW() - INTERVAL 4 HOUR, 'DIGITAL_WALLET', FALSE, 0.00, 'APPROVED', 'Lunch'),
('TXN075', 3, 88.00, 2, 'MicroCenter', 'Chicago, USA', NOW() - INTERVAL 3 HOUR, 'DEBIT_CARD', FALSE, 0.00, 'APPROVED', 'Computer accessories'),
('TXN076', 15, 425.00, 19, 'State Farm', 'Washington DC, USA', NOW() - INTERVAL 2 HOUR, 'BANK_TRANSFER', FALSE, 0.00, 'APPROVED', 'Auto insurance payment'),
('TXN077', 20, 1250.00, 2, 'Apple Store', 'San Francisco, USA', NOW() - INTERVAL 1 HOUR, 'DIGITAL_WALLET', FALSE, 0.00, 'APPROVED', 'iPhone 15 Pro');

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Verify data insertion
SELECT 'Users' as TableName, COUNT(*) as RecordCount FROM users
UNION ALL
SELECT 'User Profiles', COUNT(*) FROM user_profiles
UNION ALL
SELECT 'Merchant Categories', COUNT(*) FROM merchant_categories
UNION ALL
SELECT 'Transactions', COUNT(*) FROM transactions
UNION ALL
SELECT 'Fraud Alerts', COUNT(*) FROM fraud_alerts
UNION ALL
SELECT 'Fraud Blocks', COUNT(*) FROM fraud_blocks
UNION ALL
SELECT 'Smart Contracts', COUNT(*) FROM smart_contracts;

-- Show fraud statistics
SELECT 
    COUNT(*) as TotalTransactions,
    SUM(CASE WHEN is_fraudulent = TRUE THEN 1 ELSE 0 END) as FraudulentTransactions,
    ROUND(SUM(CASE WHEN is_fraudulent = TRUE THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) as FraudPercentage,
    COUNT(DISTINCT user_id) as AffectedUsers
FROM transactions;

-- Show alert breakdown by severity
SELECT 
    severity,
    COUNT(*) as AlertCount,
    status,
    COUNT(*) as StatusCount
FROM fraud_alerts
GROUP BY severity, status
ORDER BY severity;

-- Show blockchain integrity
SELECT 
    COUNT(*) as TotalBlocks,
    SUM(CASE WHEN is_valid = TRUE THEN 1 ELSE 0 END) as ValidBlocks,
    MIN(block_index) as FirstBlock,
    MAX(block_index) as LastBlock
FROM fraud_blocks;

-- Show smart contract status
SELECT 
    is_active,
    COUNT(*) as ContractCount,
    SUM(execution_count) as TotalExecutions
FROM smart_contracts
GROUP BY is_active;

-- ============================================
-- END OF INSERT SCRIPT
-- ============================================