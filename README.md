# SOMIOD - Distributed Lighting Control System

A Service-Oriented Architecture (SOA) project for IoT device management, utilizing a middleware API (SOMIOD), MQTT protocol, and SQL Server for persistence.

## 📌 Overview

This project simulates a smart lighting ecosystem where devices communicate through a central middleware. It demonstrates resource management, notification triggers, and real-time communication between distributed components.

### 🏗️ Architecture

The system consists of four main modules:
1. **SOMIOD API**: A RESTful middleware that manages applications, containers, records, and notifications.
2. **FormLightBulb**: A virtual IoT device (subscriber) that reacts to MQTT messages to change its state.
3. **FormSwitch**: A control interface (publisher) that sends state changes to the API.
4. **TestMiddlewareApplication**: A comprehensive management tool to perform CRUD operations directly on the middleware resources.

**Communication Flow:**
`Switch (HTTP POST)` ➡️ `SOMIOD API (SQL Save + MQTT Pub)` ➡️ `Mosquitto Broker` ➡️ `LightBulb (MQTT Sub)`

## 🛠️ Technologies
* **Language**: C# (.NET Framework 4.7+)
* **API**: ASP.NET Web API 2
* **Database**: SQL Server
* **Messaging**: MQTT (via Mosquitto Broker)
* **Libraries**: M2Mqtt, RestSharp, System.Text.Json

## 🚀 Getting Started

### 1. Prerequisites
* **SQL Server**: Ensure an instance is running.
* **Mosquitto Broker**: Version 2.0 or higher.
* **Visual Studio**: 2019 or 2022.

### 2. Database Setup
1. Run the provided `database.sql` script to create the tables.
2. Update the `connectionString` in the `Web.config` of the **SOMIOD** project.

### 3. Mosquitto Configuration
Since Mosquitto 2.0+, you must allow anonymous connections. Create a `mosquitto.conf`:
```text
listener 1883
allow_anonymous true
