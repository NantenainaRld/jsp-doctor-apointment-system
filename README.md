# Doctor Appointment System

A complete web-based appointment management system for doctors, patients, and administrators. Built with Java JSP/Servlets and Bootstrap 5.

---

## Features

### 👤 Patient Module
- Register / Login with email and password
- View and filter appointments (by date, time, status, doctor)
- Book appointments based on doctor availability
- Cancel or modify pending appointments
- View doctor list with search/filter by specialty and rate
- View top 5 most booked doctors
- Edit personal profile
- Delete account

### 👨‍⚕️ Doctor Module
- Login with ID and password
- View and filter appointments (by patient name, date, status)
- Confirm or cancel appointments
- Send email notifications to patients
- View list of patients who booked with them
- Manage working hours (CRUD operations)
- Edit personal profile

### 🔧 Admin Module
- Secure login (hardcoded credentials)
- Full CRUD operations for doctors, patients, appointments
- View and filter all appointments
- View doctor availability slots per doctor
- Confirm/Cancel/Notify appointments on behalf of doctors
- Delete any record from the system

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | HTML5, CSS3, Bootstrap 5, FontAwesome Pro |
| Backend | Java 21, JSP, Servlets, JDBC |
| Database | MySQL / MariaDB |
| Security | BCrypt password hashing |
| Email | JavaMail API (SMTP) |
| Server | Apache Tomcat 10.1 |
| Build Tool | Maven |
| IDE | IntelliJ IDEA |

---


---

## Prerequisites

- **Java JDK 17** or higher
- **Apache Tomcat 10.1** (configured in IntelliJ)
- **MySQL / MariaDB** (version 10.x or higher)
- **Maven** (or use IntelliJ built-in)
- **IntelliJ IDEA** (Community or Ultimate)

---

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/jsp-doctor-apointment-system.git
cd jsp-doctor-apointment-system

# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/doctor_apointment_db
DB_USER=root
DB_PASSWORD=your_mysql_password

# Email Configuration (for notifications)
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your_email@gmail.com
SMTP_PASSWORD=your_app_password
SMTP_AUTH=true
SMTP_STARTTLS=true
```

### Default account
Default Accounts
Role	Credentials	Notes
Admin	Username: admin
Password: 000000	Hardcoded in servlet


### Configuration Files
IntelliJ Tomcat Deployment
Tomcat home: /usr/share/tomcat10 (Arch Linux) or your Tomcat installation path

Deployment directory: .SmartTomcat/jsp_doctor_apointment_system/jsp_doctor_apointment_system

Application context: /

Artifact: jsp_doctor_apointment_system:war exploded

Maven pom.xml
Key dependencies:

jakarta.servlet-api (version 6.0.0)

jakarta.servlet.jsp-api (version 3.1.0)

mysql-connector-java (version 8.0.33)

bcrypt (version 0.10.2)

javax.mail (version 1.6.2)

dotenv-java (version 3.0.0)

### License
This project is licensed under the MIT License.

### Author
Nantenaina RALANDISON

Acknowledgments
Bootstrap 5 team

FontAwesome for icons

BCrypt for password security

JavaMail API

JetBrains IntelliJ IDEA
