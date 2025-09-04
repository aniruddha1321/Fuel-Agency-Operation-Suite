# Fuel Agency Operation Suite (FAOS)

A comprehensive web-based management system for fuel agencies to handle customer operations, cylinder management, supplier coordination, and booking processes efficiently.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)

## Overview

The Fuel Agency Operation Suite (FAOS) is a full-stack web application designed to streamline operations for fuel distribution agencies. It provides separate interfaces for administrators and customers, enabling efficient management of gas cylinder bookings, inventory tracking, and customer service operations.

## Features

### Admin Features
- **Customer Management**: Register, view, update, and manage customer accounts
- **Supplier Management**: Add, edit, and track supplier information
- **Cylinder Management**: Monitor gas cylinder inventory, refill tracking, and availability
- **Booking Management**: Process and track customer bookings
- **Report Generation**: Generate business analytics and operational reports
- **User Authentication**: Secure admin login with session management

### Customer Features
- **Personal Dashboard**: View account information and booking history
- **Online Booking**: Request gas cylinder deliveries
- **Booking History**: Track past and current orders
- **Profile Management**: Update personal information and contact details

### General Features
- **Responsive Design**: Mobile-friendly interface that works across all devices
- **Role-based Access Control**: Separate functionalities for admins and customers
- **Real-time Updates**: Live status updates for bookings and operations
- **Secure Authentication**: Session-based login system with proper logout functionality

## 🛠 Technology Stack

### Backend
- **Java 21**: Programming language
- **Spring Boot**: Framework for building the REST API
- **Spring Data JPA**: Data persistence layer
- **Maven**: Dependency management and build tool
- **REST API**: For frontend-backend communication

### Frontend
- **Thymeleaf**: Server-side templating engine
- **HTML5/CSS3**: Markup and styling
- **Bootstrap 5.3.3**: Responsive UI framework
- **JavaScript**: Client-side functionality
- **Font Awesome**: Icons and UI elements

### Database
- **JPA/Hibernate**: ORM for database operations
- **H2/MySQL**: Database (configurable)

## Project Structure

```
Fuel-Agency-Operation-Suite/
├── FAOSBackend/                 # Backend REST API module
│   ├── src/main/java/com/faos/
│   │   ├── controller/          # REST controllers
│   │   ├── model/              # Entity classes
│   │   ├── repository/         # Data access layer
│   │   ├── service/            # Business logic
│   │   └── main/               # Main application class
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── FAOSFrontend/               # Frontend web application
│   ├── src/main/java/com/faos/
│   │   ├── controller/         # Web controllers
│   │   ├── model/             # DTOs and view models
│   │   └── config/            # Configuration classes
│   ├── src/main/resources/
│   │   ├── templates/         # Thymeleaf templates
│   │   │   ├── fragments/     # Reusable components
│   │   │   └── *.html         # Page templates
│   │   └── static/
│   │       ├── css/           # Stylesheets
│   │       ├── js/            # JavaScript files
│   │       └── images/        # Static images
│   └── pom.xml
│
└── README.md
```

## Installation

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Git

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/aniruddha1321/Fuel-Agency-Operation-Suite.git
   cd Fuel-Agency-Operation-Suite
   ```

2. **Build and run the Backend**
   ```bash
   cd FAOSBackend
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   Backend will start on `http://localhost:8080`

3. **Build and run the Frontend** (in a new terminal)
   ```bash
   cd FAOSFrontend
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
   Frontend will start on `http://localhost:8081` (or next available port)

4. **Access the application**
   Open your browser and navigate to the frontend URL

## Usage

### Admin Login
- Use admin credentials to access the admin dashboard
- Manage customers, suppliers, cylinders, and view reports
- Process customer bookings and track operations

### Customer Login
- Customers can register or log in to their accounts
- View personal dashboard with booking history
- Place new gas cylinder orders
- Update profile information

### Default Credentials
```
Admin: 
- Username: admin
- Password: admin123

Customer: 
- Register new account or use existing customer ID
```

## API Endpoints

### Authentication
- `POST /login` - User authentication
- `GET /logout` - User logout

### Customer Management
- `GET /customers` - List all customers (Admin only)
- `POST /register` - Register new customer
- `GET /customer/dashboard` - Customer dashboard
- `PUT /update/{id}` - Update customer information

### Booking Management
- `POST /booking` - Create new booking
- `GET /history` - View booking history

### Cylinder Management
- `GET /cylinders` - List cylinders (Admin only)
- `POST /add-cylinder` - Add new cylinder
- `PUT /update-cylinder` - Update cylinder information
- `DELETE /delete-cylinder` - Remove cylinder

### Supplier Management
- `GET /suppliers` - List suppliers (Admin only)
- `POST /addSupplier` - Add new supplier
- `PUT /suppliers/edit/{id}` - Update supplier
