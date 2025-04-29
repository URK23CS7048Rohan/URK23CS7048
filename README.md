# Personalized Healthcare Application

A Java-based personalized healthcare application built with Spring Boot that can be deployed on a free cloud service. The application includes user authentication, health tracking, appointment management, and can work with or without a database.

## Features

- User registration and login
- Personal health profile management
- Health metrics tracking (blood pressure, heart rate, etc.)
- Appointment scheduling and management
- Responsive and modern UI
- Database flexibility: Works with MySQL or falls back to H2 in-memory database

## Technology Stack

- **Backend**: Java 11, Spring Boot 2.7.x
- **Frontend**: Thymeleaf, Bootstrap 5
- **Database**: MySQL (primary), H2 (fallback)
- **Security**: Spring Security
- **Build Tool**: Maven

## Project Structure

```
personalized-healthcare/
├── src/main/java/com/healthcare/
│   ├── config/           # Configuration classes
│   ├── controller/       # MVC controllers
│   ├── model/            # Entity classes
│   ├── repository/       # Data repositories
│   ├── service/          # Business logic
│   └── PersonalizedHealthcareApplication.java
├── src/main/resources/
│   ├── static/           # CSS, JS, images
│   ├── templates/        # Thymeleaf templates
│   └── application.properties
└── pom.xml
```

## Setup Instructions

### Prerequisites

- Java 11 or higher
- Maven
- MySQL (optional)

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```
   mvn clean install
   ```
4. Run the application:
   ```
   mvn spring-boot:run
   ```
5. Access the application at http://localhost:8080

### Database Configuration

The application is configured to work with both MySQL and H2:

- **MySQL**: If available, the application will connect to a MySQL database at `jdbc:mysql://localhost:3306/healthcare`
- **H2 (Fallback)**: If MySQL is unavailable, it will automatically use an H2 in-memory database
- **H2 Console**: Available at http://localhost:8080/h2-console when running with H2

## Deployment

This application can be deployed to various free cloud platforms:

### Deploying to Heroku

1. Create a Heroku account and install the Heroku CLI
2. Create a new Heroku app:
   ```
   heroku create personalized-healthcare
   ```
3. Add a database add-on:
   ```
   heroku addons:create jawsdb:kitefin
   ```
4. Deploy the application:
   ```
   git push heroku main
   ```

### Deploying to Railway

1. Create a Railway account
2. Connect your GitHub repository
3. Add a MySQL database service
4. Configure environment variables to match application.properties
5. Deploy from the Railway dashboard

## Default Credentials

The application is pre-configured with a default admin user:

- Username: admin
- Password: admin123

## License

This project is licensed under the MIT License - see the LICENSE file for details.