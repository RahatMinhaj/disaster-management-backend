# Disaster Management Application

This repository contains a Java application that demonstrates how to set up a database connection in a development environment using Spring Boot. Follow the instructions below to get started.

## Prerequisites

Before you begin, ensure you have the following installed:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) (version 11 or higher)
- [Maven](https://maven.apache.org/download.cgi) (for dependency management and build)
- [Oracle Database](https://www.oracle.com/database/technologies/) or any other database of your choice
- An IDE (e.g., [IntelliJ IDEA](https://www.jetbrains.com/idea/), [Eclipse](https://www.eclipse.org/))

## Getting Started

### 1. Clone the Repository

Clone this repository to your local machine using the following command:

```bash
git clone https://github.com/yourusername/repository-name.git
cd repository-name

```

### 1. Configure db connection in "src/main/resources/config/application-dev.yml"
```bash
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:xe  # Change as per your database URL
    username: your_db_username                    # Your database username
    password: your_db_password                    # Your database password

