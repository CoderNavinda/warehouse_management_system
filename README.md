# Warehouse Management System (WMS)

This project aims to manage the processes of a warehouse using a software based on micro services architecture.This project has functionalities for inventory management, order processing, billing and invoicing, shipping, and notifications.

---

## Features

- **Built using Spring Boot using microservices architecture**
- **Kafka for communication between services.**
- **Keycloak for authentication and API Gateway for routing.**
- **PostgreSQL based relational database.**
- **Sends notifications for events like order creation and payment completion.**
- **Handles invoices and payments.**

---

## Tech Stack

- **Languages**: Java
- **Frameworks**: Spring Boot, Spring Cloud
- **Database**: PostgreSQL
- **Other**: Apache Kafka, keycloack, docker

---

## How to Run

- clean install each microservice independently using 'mvn clean install' command.
- Start each microservice independently using 'mvn spring-boot:run'.
- Ensure PostgreSQL and Kafka are running and configured properly.
- Start Keycloak and configure it to with the API Gateway.
- Start the API Gateway to route requests using POSTMAN
