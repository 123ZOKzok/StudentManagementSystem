
# Student Management System

A full-stack application for managing student records with secure login, data generation, processing, and reporting features.

![App Screenshot](screenshot.png)

---

## Features

- **JWT-based Authentication**  
- **Student Management** (CRUD, Photo upload)  
- **Data Generation** to Excel (up to 1 million records)  
- **Data Processing** (Excel → CSV with +10 scores)  
- **Data Upload** (Excel → DB with +5 scores)  
- **Reporting & Export** (Filtered Excel report, Pagination)  
- **Responsive UI** (Angular 18, Tailwind CSS)

---

## Tech Stack

| Layer     | Tech                                     |
|-----------|------------------------------------------|
| Frontend  | Angular 18, Tailwind, ngx-toastr, Auth0  |
| Backend   | Spring Boot 3.2, Java 17, Spring Security, JWT |
| DB        | Microsoft SQL Server                     |
| File Ops  | Apache POI, OpenCSV                      |

---

## Folder Structure

### Backend

```
backend/
├── src/main/java/org/example/
│   ├── config/             # Spring configuration
│   ├── controller/         # REST APIs
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA Entities
│   ├── exception/          # Exceptions
│   ├── repository/         # Repositories
│   ├── security/           # JWT config
│   ├── service/            # Business logic
│   └── util/               # Utility classes
├── src/main/resources/
│   ├── application.properties
│   └── data.sql            # Test users seeded at startup
├── pom.xml
```

### Frontend

```
frontend/
├── src/app/
│   ├── components/         # UI Components
│   ├── guards/             # Auth Guards
│   ├── interceptors/       # JWT Interceptors
│   ├── models/             # TypeScript models
│   ├── pages/              # Dashboard, Login, etc.
│   ├── services/           # API services
│   ├── app.routes.ts       # Routes
│   └── app.config.ts       # App settings
├── environments/
│   ├── environment.ts
├── angular.json
├── package.json
```

---

## Test User (Pre-loaded)

| Username | Password      |
|----------|---------------|
| `admin`  | `password123` |

---

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **Node.js 18+**
- **Angular CLI 18+**
- **Microsoft SQL Server**

---

## Installation Guide

### Backend

```bash
cd backend
# Set DB credentials in src/main/resources/application.properties
./mvnw clean install
java -Xmx1024m -jar target/student-api.jar
```

### Frontend

```bash
cd frontend
npm install
# Set API URL in src/environments/environment.ts
ng serve
```

---

## API Endpoints

| Endpoint                                 | Method | Description                    |
|------------------------------------------|--------|--------------------------------|
| `/api/auth/login`                        | POST   | User login                     |
| `/api/students`                          | GET    | Get all students               |
| `/api/students/{id}`                     | GET    | View student details           |
| `/api/students/{id}`                     | PUT    | Edit student                   |
| `/api/students/{id}`                     | DELETE | Soft delete student            |
| `/api/students`                          | POST   | Create new student             |
| `/api/data-generation/generate-students`| POST   | Generate Excel data            |
| `/api/data-processing/process-excel`    | POST   | Convert Excel to CSV (+10)     |
| `/api/file-upload/students`             | POST   | Upload Excel to DB (+5)        |

---

## Development & Testing

### Backend

```bash
./mvnw test
```

### Frontend

```bash
npm run test
```

---

## Build for Production

### Backend

```bash
./mvnw clean package
```

### Frontend

```bash
npm run build
```

---

## Environment Variables

### Backend (`application.properties`)

```
spring.datasource.url=jdbc:sqlserver://localhost;databaseName=studentdb
spring.datasource.username=sa
spring.datasource.password=your_password
app.jwt.secret=your_secret_key
app.jwt.expiration-ms=86400000
```

### Frontend (`environment.ts`)

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

---

## Demo Walkthrough (Video)
