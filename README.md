# CRM Pro — Customer Relationship Management

A full-stack CRM application built with **Spring Boot** (backend) and **React + Vite** (frontend).

![CRM Dashboard](https://img.shields.io/badge/Status-Active-brightgreen) ![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F) ![React](https://img.shields.io/badge/React-19-61DAFB)

---

## 🚀 Features

- **JWT Authentication** — Secure login with role-based access (Admin, Manager, Sales Rep)
- **Customer Management** — Full CRUD with lifecycle status tracking, search & pagination
- **Contacts** — Manage individual contacts linked to customer accounts
- **Deals Pipeline** — Kanban board + list view with 6-stage pipeline, value & probability tracking
- **Task Management** — Priority/status tracking with overdue detection
- **Activity Logging** — Track calls, emails, meetings, and notes
- **Dashboard Analytics** — Real-time KPIs across all CRM modules
- **Data Seeding** — Pre-loaded sample data for immediate testing

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 17, Spring Boot 3.2.3, Spring Data JPA, Spring Security |
| **Database** | H2 (in-memory, dev) — easily swappable to PostgreSQL/MySQL |
| **Auth** | JWT (JSON Web Tokens) |
| **Frontend** | React 19, Vite, React Router, Lucide Icons |
| **Styling** | Custom CSS with dark theme design system |

---

## 📦 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+

### Backend
```bash
cd crm-backend
mvn clean compile spring-boot:run
```
Backend starts on **http://localhost:8081/api**

### Frontend
```bash
cd crm-frontend
npm install
npm run dev
```
Frontend starts on **http://localhost:5173**

---

## 🔑 Demo Credentials

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | Admin |
| `jmanager` | `manager123` | Manager |
| `bsalesrep` | `sales123` | Sales Rep |
| `asalesrep` | `sales123` | Sales Rep |

---

## 📡 API Endpoints

| Module | Endpoints | Auth |
|--------|-----------|------|
| **Auth** | `POST /api/auth/login`, `POST /api/auth/register` | Public |
| **Customers** | CRUD + search + filter by status | Bearer Token |
| **Contacts** | CRUD scoped to customers | Bearer Token |
| **Deals** | CRUD + pipeline stages + search | Bearer Token |
| **Tasks** | CRUD + status/priority filtering | Bearer Token |
| **Activities** | CRUD + type filtering | Bearer Token |
| **Dashboard** | `GET /api/dashboard` | Bearer Token |

---

## 📁 Project Structure

```
crm-backend/
├── src/main/java/com/crm/
│   ├── config/          # Security, CORS, Data Seeder
│   ├── controller/      # REST Controllers
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # JPA Entities
│   ├── enums/           # Status, Stage, Priority enums
│   ├── exception/       # Global error handling
│   ├── repository/      # JPA Repositories
│   ├── security/        # JWT Provider, Filter, UserDetails
│   └── service/         # Business Logic

crm-frontend/
├── src/
│   ├── components/      # Sidebar, reusable components
│   ├── context/         # Auth context
│   ├── pages/           # Dashboard, Customers, Deals, Tasks, Activities
│   ├── services/        # API service layer
│   └── index.css        # Design system
```

---

## 📝 License

MIT
