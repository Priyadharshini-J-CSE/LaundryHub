# Hostel Washing Application

A comprehensive washing management system for hostels built with Spring Boot, MongoDB, and Thymeleaf.

## Features

### For Students:
- **Dashboard**: Overview of all services
- **Stones**: Book washing stones with availability status
- **Machines**: Book washing machines with time slots
- **Dhobi Service**: Request laundry service and track status
- **Notifications**: Get updates on availability and requests
- **Lost & Found**: Report lost items with image upload support

### For Dhobis:
- **Dashboard**: Manage laundry and lost item requests
- **Laundry Requests**: Accept/reject student laundry requests
- **Lost Requests**: Respond to student lost item reports

## Tech Stack
- **Backend**: Java Spring Boot 3.1.0
- **Database**: MongoDB
- **Frontend**: Thymeleaf, Bootstrap 5
- **Security**: Spring Security

## Setup Instructions

### Prerequisites
- Java 17 or higher
- MongoDB installed and running on localhost:27017
- Maven

### Installation
1. Clone the repository
2. Navigate to project directory
3. Start MongoDB service
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the application at `http://localhost:8080`

### Default Users
- **Student**: username: `student1`, password: `password`
- **Dhobi**: username: `dhobi1`, password: `password`

### Sample Data
The application automatically initializes with:
- 6 washing stones (S1-S6)
- 4 washing machines (M1-M4)
- Sample user accounts

## Usage
1. Register as Student or Dhobi
2. Login with your credentials
3. Access role-specific dashboard
4. Use the navigation menu to access different features

## API Endpoints
- `GET /` - Redirect to login
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `GET /student/*` - Student dashboard and features
- `GET /dhobi/*` - Dhobi dashboard and features

## Database Collections
- `users` - User accounts with roles
- `stones` - Washing stones with booking status
- `machines` - Washing machines with booking status
- `laundry_requests` - Student laundry service requests
- `lost_found` - Lost item reports and responses