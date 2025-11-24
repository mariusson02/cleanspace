# Coding Conventions for CleanSpace

**These Coding Conventions are based on and extend
the [Google Java Style Guide](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml)**

## Usage

Run `mvn checkstyle:check` to test for code style errors.

Run `mvn spotless:apply` to automatically format the code base according to the style guide (also warnings).

## Preamble / Introduction

### Background
This document establishes coding standards for the CleanSpace project, a domain-driven Spring Boot application for workspace management. These conventions ensure consistent code quality, maintainability, and team collaboration.

### Goals
- **Consistency**: Uniform code style across all team members and project modules
- **Readability**: Clear, self-documenting code that reduces cognitive load
- **Maintainability**: Standards that facilitate long-term code evolution and debugging
- **Quality Assurance**: Automated enforcement through checkstyle and code review processes

### Scope
These conventions apply to:
- All Java source code in the CleanSpace project
- Configuration files (application.properties, docker-compose.yaml)
- Documentation and API contracts
- Test code following the same standards as production code

### Compliance Requirements
- **Mandatory**: All code must pass `mvn checkstyle:check` before commit
- **Code Reviews**: Adherence to these conventions is verified during pull request reviews
- **Automated Formatting**: Use `mvn spotless:apply` to ensure consistent formatting

### Keywords (RFC 2119)
All the listed conventions below are a **MUST** / **REQUIRED**: Absolute requirements that cannot be deviated from

### Document Structure
1. **Source Code Organisation**: Project structure and module dependencies
2. **Naming Conventions**: Naming rules for classes, methods, and resources
3. **Formatting**: Code layout, indentation, and visual structure
4. **Comments**: Documentation standards and commenting guidelines
5. **Language Features**: Java and Spring Boot best practices

## Source Code Organisation
```
cleanspeace/
├── application/
│   ├── src/
│   │   └── main/
│   │   │   └── java/
│   │   │       └── de.schonvoll.cleanspace.application 
│   │   │           ├── commands/                            
│   │   │           └── services/                           
│   │   └── test/
│   │       └── java/
│   │           └── de.schonvoll.cleanspace.application 
│   │               ├── doubles/                             
│   │               ├── services/
│   │               └── constants     
│   ├── target/
│   │   └──  stylecheck(s).xml
│   └── pom.xml
├── config/
│   └── checkstyle/                                                               
│       └── google_checks.xml
├── docs/ 
├── domain/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── de.schonvoll.cleanspace.domain/
│   │               ├── entities/                               
│   │               ├── exceptions/
│   │               ├── repositories/
│   │               ├── services/
│   │               └── valueobjects/                              
│   ├── target/
│   │   └── checkstyle.xml 
│   └── pom.xml            
├── infrastructure/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── de.schonvoll.cleanspace.infrastructure/
│   │               ├── inmemory/                               
│   │               ├── postgres/
│   │               │   ├── entities
│   │               │   ├── jpa
│   │               │   ├── mapper
│   │               │   └── repositories
│   │               └── security/
│   ├── target/
│   │   └── checkstyle.xml 
│   └── pom.xml  
├── main/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── de.schonvoll.cleanspace.main/
│   │       │       ├── config/
│   │       │       └── CleanSpaceApplication.java
│   │       └── resources/ 
│   │           └── .properties
│   ├── target/
│   │   └── checkstyle.xml 
│   └── pom.xml 
├── presentation/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── de.schonvoll.cleanspace.http/
│   │               ├── controllers/
│   │               ├── dtos/
│   │               └── GlobalExceptionHandler.java
│   ├── target/
│   │   └── checkstyle.xml 
│   └── pom.xml                                                                                   
├── target/
│   └── checkstyle.xml
├── .env
├── api-test.http
├── CODING_CONVENTIONS.md
├── Dockerfile
├── docker-compose.yaml
├── pom.xml
└── README.md                            
```
**Project Structure Description:**

- **application/**: Application layer containing use cases and command handlers
  - **commands/**: Command objects and command handlers for CQRS pattern
  - **services/**: Application services orchestrating domain operations
  - **doubles/**: Test doubles (mocks, stubs) for unit testing
  - **constants**: Test constants and shared test data


- **config/**: Project-wide configuration files
  - **checkstyle/**: Code style checking configuration (Google Java Style Guide)


- **docs/**: Contains planing


- **domain/**: Core business logic and domain model (dependency-free)
  - **entities/**: Domain entities with business rules and behavior
  - **exceptions/**: Domain-specific exceptions and error handling
  - **repositories/**: Repository interfaces (contracts for data access)
  - **services/**: Domain services for complex business operations
  - **valueobjects/**: Immutable value objects representing domain concepts


- **infrastructure/**: External concerns and technical implementations
  - **inmemory/**: In-memory implementations for testing and development
  - **postgres/**: PostgreSQL database integration
    - **entities**: JPA entity mappings for database persistence
    - **jpa**: JPA repository implementations and specifications
    - **mapper**: Data mappers between domain and persistence models
    - **repositories**: Concrete implementations of domain repository interfaces
  - **security/**: Security configuration and authentication mechanisms


- **main/**: Application entry point and bootstrap configuration
  - **config/**: Spring Boot configuration classes and beans
  - **CleanSpaceApplication.java**: Main Spring Boot application class
  - **resources/**: Application properties and configuration files


- **presentation/**: HTTP API layer and external interfaces
  - **controllers/**: REST controllers handling HTTP requests
  - **dtos/**: Data Transfer Objects for API contracts
  - **GlobalExceptionHandler.java**: Centralized exception handling for HTTP responses


- **Root Level Files:**
  - **.env**: Environment variables for local development
  - **api-test.http**: HTTP requests for API testing
  - **Dockerfile**: Container configuration for deployment
  - **docker-compose.yaml**: Multi-container application setup
  - **pom.xml**: Maven project configuration and dependencies
  - **target/**: Generated build artifacts and checkstyle reports

## Naming Conventions

- **Classes**: PascalCase
```java
  public class UserService { }
  public class BookingController { }
  public class SpaceRepository { }
```
  

- **Methods/Variables**: camelCase
```java
public User findUserById(Long id) { }
private BigDecimal orderTotal;
boolean isBookingAvailable;
```

- **Constants**: UPPER_SNAKE_CASE
```java
public static final int MAX_RETRY_COUNT = 3;
private static final String DEFAULT_CURRENCY = "EUR";
public static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);
```

- **REST Endpoints**: kebab-case
```
@GetMapping("/api/user-bookings")
@PostMapping("/api/space-availability")
@DeleteMapping("/api/booking-cancellation/{id}")
```

- **Database Tables**: snake_case
```
@Table(name = "user_profile")
@Table(name = "booking_history")
@Table(name = "space_availability")
```

- **Spring Beans**: camelCase matching the class
```
@Service("userService")
@Repository("bookingRepository")
@Component("emailNotificationService")
```

- **Test Methods**: should_action_when_condition format
```java
void should_return_user_when_valid_id_provided() { }
void should_throw_exception_when_booking_not_found() { }
void should_create_booking_when_space_is_available() { }
```

## Formatting

- **Indentation**: 2 spaces, no tabs
- **Line Length**: Maximum 150 characters
- **File Length**: Soft maximum 200, hard maximum 500 lines
- **Brace Style**: K&R Style (opening brace on same line)
- **Blank Lines**: One between methods, two between classes
- **Import Order**: Static imports first, then alphabetical
- **No Wildcard Imports** except for static imports in tests
- **Remove Trailing Whitespace**

## Comments

- **JavaDoc** required for all non-trivial public methods and classes
- **Inline Comments** only for complex business logic and for explaining the why
- **TODO Comments** with ticket number and date
- **Spring Annotations** document the purpose of the component
- **No commented-out code** in commits
- **API Documentation** with `@ApiOperation` for REST endpoints if non-trivial
- **All Comments in English**

## Language Features & Policies

- **Java Version**: Use minimum Java 17 features
- **Optional**: Use instead of null-checks where appropriate
- **Stream API**: Prefer for collection operations
- **Lambda Expressions**: Keep short and expressive
- **Exception Handling**: Specific exceptions, no generic catches
- **Spring Annotations**: Use `@Component`, `@Service`, `@Repository` correctly
- **Dependency Injection**: Prefer constructor injection
- **Immutable Objects**: Use where possible (`@Value`, `record`)
- **Null Safety**: Use `@NonNull`, `@Nullable` annotations