# CleanSpace - Co-Working Space Reservation System

CleanSpace is a backend application for managing and reserving workspaces in a co-working space. The
system is developed as a server application based on the Domain Driven Design Pattern and allows users to find and book workspaces
based on availability, capacity, and specific characteristics (e.g., monitors, docking stations,
etc.).
Interaction with the system takes place via a RESTful HTTP API.

## My Contributions

I worked on this project as part of the team. My personal contributions include:

- Implementing the domain entities and related value objects.
- Designing and implementing use-cases (application layer) and application services.
- Writing API/system tests (see `api-test.http`) to verify endpoints and flows.

I am listing my contributions here for portfolio purposes; the project was developed by a four-person student team.

## Tech Stack

- **Java 21** - Programming language
- **Spring Boot 3.x** - Application framework
- **Maven** - Build and dependency management
- **PostgreSQL** - Production database
- **H2** - In-memory database for development
- **Docker & Docker Compose** - Containerization

## Architecture

This application follows Clean Architecture principles with clear separation of concerns across different layers:

- **Domain Layer** - Business logic and entities
- **Use Case Layer** - Application-specific business rules
- **Interface Adapters** - Controllers, presenters, and gateways
- **Infrastructure Layer** - External concerns (database, web framework)

## API Endpoints

The application provides a RESTful HTTP API with the following main endpoints:

- `POST /api/auth/login` - User authentication
- `GET /api/workspaces` - List all workspaces
- `GET /api/workspaces/available` - Find available workspaces
- `POST /api/reservations` - Create a reservation
- `GET /api/reservations` - Get user reservations
- `PUT /api/reservations/{id}` - Update reservation
- `DELETE /api/reservations/{id}` - Cancel reservation

## Project Topics and Use Cases

### CleanSpace: A Reservation System for Co-Working Spaces

CleanSpace is a backend application for managing and reserving workspaces in a co-working space. The system is developed as a server application following the Clean Architecture pattern and enables users to find and book workspaces based on availability, capacity, and specific features (e.g. monitor equipment, docking station, etc.). A central feature is the support of both one-time and complex recurring reservations (e.g. daily, weekly), including the ability to reschedule or cancel individual appointments in a series. Interaction with the system is via a RESTful HTTP API.

### Use Cases

1. Login (LoginUser)
2. Show Available Workspaces (FindAvailableWorkspaces)
3. Book Workspace (CreateReservation)
4. Show Workspace Bookings (FindUserReservations)

#### Additional Use Cases

1. CreateReservationSeries
2. UpdateReservation
3. Cancel Reservation
4. ListWorkspaces
5. FindAvailableTimeSlotsForWorkspace

## How to use

### start local

- create .env file
  ```.env
      SPRING_PROFILES_ACTIVE=in-memory
  ```
- run `java -jar ./main/target/cleanspace-api.jar`

### start docker

- run `docker compose up`

## Run System Tests

- open and run `api-test.http` in IntelliJ after having started the application

## Initialized Data

```yaml
users:
  - email: admin@cleanspace.de
    password: password# cleanspace
