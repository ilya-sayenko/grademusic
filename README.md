# Grademusic
Application for grade music albums.

## Stack
Java, Spring Boot, PostgreSQL, Kafka, Redis.

## Сomposition of services

### grademusic-gateway
API Gateway service. Using [OpenAPI Route Definition Locator](https://github.com/jbretsch/openapi-route-definition-locator) for creating route definitions.

### grademusic-auth-service
Authentication and authorization service via JWT token.

### grademusic-main-service
Main service. Provides functionality:
- Search music album by name
- Add album to wishlist
- Calculate album and user statistics (count of grades, average grade, ...)
