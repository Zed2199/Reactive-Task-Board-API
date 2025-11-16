# Reactive Task Board

A modern, reactive task management REST API built with Spring Boot 3, Spring WebFlux, and MongoDB. This application demonstrates reactive programming principles using Project Reactor and provides real-time task event streaming capabilities.

## Features

- **Reactive Architecture**: Built with Spring WebFlux for non-blocking, asynchronous request handling
- **MongoDB Integration**: Reactive MongoDB repository for efficient data access
- **Real-time Updates**: Server-Sent Events (SSE) for streaming task changes
- **CRUD Operations**: Complete task management with validation
- **Pagination Support**: Efficient pagination for large task lists
- **Filtering**: Filter tasks by status and priority
- **DTO Pattern**: Clean separation between domain models and API contracts
- **Exception Handling**: Centralized error handling with custom exceptions
- **Auto-Seeding**: Automatic database population with dummy data for testing
- **MapStruct Integration**: Type-safe object mapping

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring WebFlux** (Reactive Web)
- **Spring Data MongoDB Reactive**
- **MongoDB** (NoSQL Database)
- **Project Reactor** (Reactive Streams)
- **Lombok** (Boilerplate reduction)
- **MapStruct** (Object mapping)
- **Bean Validation** (Request validation)
- **Gradle** (Build tool)

## Prerequisites

- Java 21 or higher
- Docker and Docker Compose (recommended) OR MongoDB 4.4+ running locally
- Gradle 7+ (or use the included wrapper)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/zed2199/reactive-task-board.git
cd reactive-task-board
```

### 2. Start MongoDB with Docker Compose (Recommended)

The easiest way to get started is using the provided Docker Compose configuration:

```bash
cd docker
docker-compose up -d
```

This will start:
- **MongoDB** on port `27017` (credentials: `root/root`)
- **Mongo Express** (Web UI) on port `8081` at `http://localhost:8081`

The application is already configured to connect to this Docker setup.

#### Alternative: Manual MongoDB Setup

If you prefer to use your own MongoDB instance, update `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://username:password@host:port/taskboard?authSource=admin
```

### 3. Build the Project

```bash
./gradlew build
```

### 4. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### 5. Auto-Seeding

On first startup, the application automatically seeds the database with 50 random tasks if the database is empty. This provides immediate test data for exploring the API.

## API Endpoints

### Task Management

#### Get All Tasks
```http
GET /api/tasks
```

Query Parameters:
- `status` (optional): Filter by status (`TODO`, `IN_PROGRESS`, `DONE`)
- `priority` (optional): Filter by priority (`LOW`, `MEDIUM`, `HIGH`)

Example:
```bash
curl http://localhost:8080/api/tasks?status=TODO&priority=HIGH
```

#### Get Tasks (Paginated)
```http
GET /api/tasks/paged
```

Query Parameters:
- `page` (default: 0): Page number (zero-based)
- `size` (default: 10): Number of items per page
- `status` (optional): Filter by status
- `priority` (optional): Filter by priority

Example:
```bash
curl "http://localhost:8080/api/tasks/paged?page=0&size=20&status=TODO"
```

#### Get Task by ID
```http
GET /api/tasks/{id}
```

Example:
```bash
curl http://localhost:8080/api/tasks/507f1f77bcf86cd799439011
```

#### Create Task
```http
POST /api/tasks
Content-Type: application/json
```

Request Body:
```json
{
  "title": "Implement user authentication",
  "description": "Add JWT-based authentication to the API",
  "status": "TODO",
  "priority": "HIGH",
  "dueDate": "2025-12-31T23:59:59Z"
}
```

Example:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Task",
    "description": "Task description",
    "status": "TODO",
    "priority": "MEDIUM"
  }'
```

#### Update Task
```http
PUT /api/tasks/{id}
Content-Type: application/json
```

Request Body:
```json
{
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2025-12-31T23:59:59Z"
}
```

Example:
```bash
curl -X PUT http://localhost:8080/api/tasks/507f1f77bcf86cd799439011 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Task",
    "status": "DONE"
  }'
```

#### Delete Task
```http
DELETE /api/tasks/{id}
```

Example:
```bash
curl -X DELETE http://localhost:8080/api/tasks/507f1f77bcf86cd799439011
```

### Real-time Streaming

#### Stream Task Events (SSE)
```http
GET /api/tasks/stream
Accept: text/event-stream
```

Streams real-time events for task creation, updates, and deletions.

Example:
```bash
curl -N http://localhost:8080/api/tasks/stream
```

Example response:
```
event: CREATED
data: {"type":"CREATED","task":{"id":"...","title":"New Task",...}}

event: UPDATED
data: {"type":"UPDATED","task":{"id":"...","title":"Updated Task",...}}

event: DELETED
data: {"type":"DELETED","task":{"id":"...","title":"Deleted Task",...}}
```

## Data Models

### Task Status
- `TODO`: Task is pending
- `IN_PROGRESS`: Task is being worked on
- `DONE`: Task is completed

### Priority Levels
- `LOW`: Low priority
- `MEDIUM`: Medium priority
- `HIGH`: High priority

### Task Response
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "Task title",
  "description": "Task description",
  "status": "TODO",
  "priority": "MEDIUM",
  "dueDate": "2025-12-31T23:59:59Z",
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-02T15:30:00Z"
}
```

## Project Structure

```
src/main/java/com/example/taskboard/
├── config/                      # Configuration classes
│   ├── DataSeeder.java          # Database seeding
│   ├── MongoConfig.java         # MongoDB configuration
│   ├── TaskRouterConfig.java    # Functional routing config
│   └── TaskStreamConfig.java    # Event stream configuration
├── domain/                      # Domain entities
│   ├── Task.java
│   ├── TaskStatus.java
│   └── Priority.java
├── repository/                  # Data access layer
│   └── TaskRepository.java
├── service/                     # Business logic layer
│   ├── TaskService.java
│   └── TaskServiceImpl.java
├── web/                         # Web layer
│   ├── TaskController.java      # REST controller
│   ├── functional/              # Functional handlers
│   │   └── TaskHandler.java
│   ├── dto/                     # Data Transfer Objects
│   │   ├── CreateTaskRequest.java
│   │   ├── UpdateTaskRequest.java
│   │   ├── TaskResponse.java
│   │   ├── TaskStatsResponse.java
│   │   └── BulkCompleteRequest.java
│   ├── event/                   # Event models
│   │   └── TaskEvent.java
│   └── error/                   # Error response models
│       └── ApiError.java
├── mapper/                      # MapStruct mappers
│   └── TaskMapper.java
├── exceptions/                  # Custom exceptions
│   ├── TaskNotFoundException.java
│   └── GlobalErrorHandler.java
└── TaskboardApplication.java    # Main application class
```

## Architecture

This application follows a layered architecture:

1. **Controller Layer** (`web/`): Handles HTTP requests/responses, validation, and delegates to services
2. **Service Layer** (`service/`): Contains business logic and orchestrates repository operations
3. **Repository Layer** (`repository/`): Data access using Spring Data Reactive MongoDB
4. **Domain Layer** (`domain/`): Core business entities
5. **DTO Layer** (`web/dto/`): API contracts separate from domain models

## Reactive Patterns

The application uses several reactive patterns:

- **Non-blocking I/O**: All operations return `Mono<T>` or `Flux<T>`
- **Backpressure Handling**: Built-in with Project Reactor
- **Event Streaming**: Real-time updates using `Sinks.Many` and SSE
- **Pagination**: Memory-efficient pagination with `skip()` and `take()`

## Testing

Run tests with:

```bash
./gradlew test
```

## Building for Production

Create a production-ready JAR:

```bash
./gradlew clean build
java -jar build/libs/reactive-dev-0.0.1-SNAPSHOT.jar
```

## Configuration

Key configuration properties (in `application.yml`):

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/taskboard

server:
  port: 8080
```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK`: Successful GET/PUT requests
- `201 Created`: Successful POST requests
- `204 No Content`: Successful DELETE requests
- `400 Bad Request`: Validation errors
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server errors

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Built with Spring Boot and Project Reactor
- Inspired by modern reactive programming principles
- MongoDB for flexible, scalable data storage

## Contact

For questions or support, please open an issue on GitHub.
