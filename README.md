## 🧠 Architectural Approach (Monolith but Microservice-Ready)

The UniClub project is currently developed using a **monolithic architecture**.
However, the project structure is deliberately designed in a way that
**facilitates a smooth transition to a microservice architecture** in the future.

This approach avoids unnecessary complexity in the early stages
while keeping the system scalable and extensible as requirements grow.

---

## 📦 Package Structure and Modularity

Each core business domain within the application
is organized under its own dedicated package:

- `user` → User management
- `club` → Club operations
- `event` → Event management
- `member` → Membership and participation handling
- `notification` → Notification system
- `mail` → Email infrastructure
- `security` → Authentication and authorization
- `logging` → Centralized logging
- `exceptions` → Global exception handling
- `baseEntity` → Shared base entity structures

This structure ensures that:
- Each package represents its own **bounded context**
- Inter-package dependencies are kept to a minimum
- Relevant packages can later be extracted into
  **independent microservices** with minimal refactoring

---

## 🔄 Monolith → Microservice Transition Strategy

The project was designed with the following principles in mind:

- **Single Responsibility Principle (SRP)**  
  Each package is responsible only for its own business domain.

- **Loose coupling**  
  Shared logic is centralized in limited and well-defined packages
  such as `commonmethods`, `util`, and `baseEntity`.

- **Service-first approach**  
  A clear separation between Controller → Service → Repository layers
  is enforced throughout the application.

As a result, modules such as:
- `notification` or `mail`
- `user` or `event`

can be converted into standalone Spring Boot services when needed.

---

## 🗄️ Database Migration Structure (Why It Is Used)

Database schema management is handled using a **migration-based approach**
(`resources/db.migration`).

The main reasons for using database migrations are:

- Version-controlled database schema
- Consistency across environments (local, test, production)
- Easier migration to microservices, where each service manages
  its own database schema
- Prevention of errors caused by manual SQL changes

This approach also supports a future transition to a
**database-per-service** model.

---

## 🧩 Advantages of a Monolithic Architecture (Project-Specific)

The decision to use a monolithic architecture in this project was based on:

- Faster development and iteration cycles
- Simpler debugging and testing processes
- Avoiding early-stage microservice infrastructure overhead
- Current business domains not requiring extreme scaling

At the same time, architectural decisions were made carefully to ensure that
**a future transition to microservices is not blocked or complicated**.

---

## 🛠️ Technologies Used

### Backend
- **Java 17**  
  Chosen for modern language features and long-term LTS support.

- **Spring Boot**  
  Used to enable rapid development, auto-configuration,
  and production-ready application setup.

- **Spring Security**  
  Implemented for JWT-based authentication and authorization.

- **Spring Data JPA (Hibernate)**  
  Used for ORM-based database access and domain-driven development.

- **PostgreSQL**  
  Selected as a reliable, powerful, and scalable relational database.

---

### Database & Migration
- **Database Migration (Flyway or similar approach)**  
  Used to version database schemas, ensure environment consistency,
  and maintain microservice readiness.

---

### Security
- **JWT (JSON Web Token)**  
  Used for stateless authentication and secure inter-layer communication.

---

### Logging & Monitoring
- **Logback**  
  Used for application-level logging configuration and management.

- **Elasticsearch**  
  Used to centralize and index application logs.

- **Kibana**  
  Used for log visualization and system observability.

---

### Containerization & DevOps
- **Docker**  
  Used to containerize the application and its dependencies.

- **Docker Compose**  
  Used to orchestrate supporting services such as Elasticsearch and Kibana.

---

### Other
- **Maven**  
  Used for dependency management and build automation.

---

## ▶️ How to Run the Project

This repository contains the **backend** part of the UniClub project.
Before running the application, make sure to complete the following steps:

1. Clone the repository and navigate to the project directory.
2. Complete the required configuration in `application.properties`.
3. Start Elasticsearch and Kibana using Docker.
4. Build and run the application.

Detailed setup and execution steps are explained in the sections above.

---

## 🌐 Frontend

The frontend application of the UniClub project is developed
as a separate repository, independent from the backend.

The frontend focuses on:
- User interface
- API integrations
- Authorization and user experience

🔗 Do not forget to check the frontend repository:  
https://github.com/MuhammedSosun/uniclub-frontend
