# 🛡️ Fraud Detection System

A real-time fraud detection system that analyzes financial transactions using rule-based detection, blockchain audit trails, and smart contract automation.

---

## 📌 Overview

This system monitors payment transactions and flags suspicious activities in real-time. Each transaction runs through multiple detection rules, gets recorded on a blockchain for tamper-proof auditing, and triggers automated actions through smart contracts when fraud is detected.

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.x
- MySQL 8.0
- Apache Kafka
- Blockchain (Custom Implementation)
- Maven

---

## 🏗️ Architecture

```
┌────────────────┐      ┌────────────────┐      ┌──────────────┐
│   Client       │────▶|   REST API      │────▶│    Kafka     │
│  (Transaction) │      │  (Spring Boot) │      │   Broker     │
└────────────────┘      └──────┬─────────┘      └──────┬───────┘
                               │                       │
                               ▼                       ▼
                        ┌──────────────┐       ┌──────────────┐
                        │    Fraud     │       │    Alert     │
                        │   Engine     │       │   Consumer   │
                        └──────┬───────┘       └──────────────┘
                               │
                 ┌─────────────┼─────────────┐
                 ▼             ▼             ▼
          ┌──────────┐  ┌──────────┐  ┌──────────┐
          │  MySQL   │  │Blockchain│  │  Smart   │
          │    DB    │  │  Ledger  │  │ Contracts│
          └──────────┘  └──────────┘  └──────────┘
```

---

## ⚙️ How It Works

### 📋 Rule-Based Detection

Transactions are evaluated against four rule categories:

| Rule | Purpose |
|------|---------|
| **Amount** | Flags unusual spending patterns or high-value transactions |
| **Frequency** | Detects rapid successive transactions (possible stolen card) |
| **Location** | Identifies high-risk countries or impossible travel |
| **Time** | Flags transactions during unusual hours |

Each rule contributes to a risk score. Based on the final score, transactions are **approved**, **flagged**, or **blocked**.

---

### 📡 Kafka Integration

Kafka enables real-time event streaming:

- Transaction events published as they arrive
- Fraud alerts streamed to notification services
- Asynchronous processing keeps API fast
- Multiple consumers can react to events (email, SMS, dashboards)

---

### 🔗 Blockchain Audit Trail

Every fraud decision is permanently recorded:

- Each decision becomes a block with unique SHA-256 hash
- Blocks are cryptographically linked together
- Proof of Work mining prevents forgery
- Any tampering breaks the chain and is instantly detected

> Provides immutable audit trail for compliance and legal purposes.

---

### 📜 Smart Contracts

Automated responses when fraud is detected:

| Trigger | Action |
|---------|--------|
| Risk ≥ 80 | Block account automatically |
| Risk ≥ 60 | Freeze funds for review |
| Risk ≥ 40 | Require 2FA verification |
| Any fraud | Notify security team |

Smart contracts execute instantly without human intervention.

---

## ✅ Advantages

- **Real-time Detection** — Instant analysis before transaction completes
- **Tamper-proof Records** — Blockchain ensures decisions cannot be altered
- **Automated Response** — Smart contracts act immediately
- **Scalable** — Kafka enables horizontal scaling
- **Configurable** — Rules can be adjusted without code changes
- **Audit Ready** — Complete history for regulatory compliance

---

## ❌ Disadvantages

- **Mining Latency** — Blockchain adds slight delay per transaction
- **Storage Growth** — Blockchain data grows continuously
- **Complexity** — Multiple components increase maintenance
- **False Positives** — Rule-based systems may flag legitimate transactions
- **No ML** — Current version doesn't use machine learning

---



## 🚀 Quick Start

**Prerequisites:** Java 17, MySQL, Apache Kafka

1. Clone repository
2. Create database `frauddetection` in MySQL
3. Update `application.yml` with your credentials
4. Start Kafka & Zookeeper
5. Run `mvn spring-boot:run`

---

## 🔮 Future Improvements

- Add machine learning model for pattern detection
- Implement multi-node distributed blockchain
- Build admin dashboard for monitoring
- Add support for more payment types

---

