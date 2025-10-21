# Lab 3 Complete a Web API -- Project Report

**Author:** Ariana Porroche Llorén (874055)

**Date:** 21th October 2025

**Course:** Web Engineering

## Description of Changes
This project extends a basic employee management API with full CRUD operations, robust error handling, security, and API documentation. The key changes include:
- Completed the SETUP and VERIFY blocks in `ControllerTests` to validate all HTTP methods (`POST`, `GET`, `PUT`, `DELETE`) for safety and idempotency.
- Verified CRUD operations work correctly in combination with the repository.
- CI/CD Integration with GitHub Actions
    - Ensures all changes pass build and test checks

### Bonus opportunities
1. **Security and Authentication and Authorization**
- **Security Configuration**: Added JWT-based authentication and role-based authorization (ADMIN, USER). Configured which HTTP methods require which roles.
  - **ADMIN**: Full access to all employee endpoints.
    - `GET /employees` → list all employees
    - `GET /employees/{id}` → get a single employee
    - `POST /employees` → create a new employee
    - `PUT /employees/{id}` → update an employee
    - `DELETE /employees/{id}` → delete an employee
  - **USER**: Limited access to read-only operations.
    - `GET /employees` → list all employees
    - `GET /employees/{id}` → get a single employee
    - Cannot create, update, or delete employees (`POST`, `PUT`, `DELETE` forbidden)
  - **Unauthenticated / Public**:
    - `POST /login` → obtain JWT token
    - Swagger/OpenAPI docs accessible at `/v3/api-docs` and `/swagger-ui/**`
- EmployeeController: Modified the access to CRUD operations on employees (`GET`, `POST`, `PUT`, `DELETE`)

- Added `JwtUtil` and `JwtFilter` to validate tokens and populate SecurityContext.

2. **Implement RESTful API Documentation with OpenAPI/Swagger**
- Modified `EmployeeController` for interactive API documentation at:
    - `/swagger-ui/index.html`
    - `/v3/api-docs`
- Included request/response examples, HTTP status codes, and error scenarios.
- Documentation available at: 
    - http://localhost:8080/swagger-ui/index.html

3. **Add Advanced Error Handling with RFC 7807 Problem Details**
- Added `Exceptions` class with:
    - `EmployeeNotFoundException`: when employee ID is missing.
    - `EmployeeAlreadyExistsException`: when trying to create an existing employee.
    - `NoEmployeesFoundException`: when repository is empty.
- Added `@ExceptionHandler` methods in `EmployeeController` to return structured `ProblemDetail` objects with `title`, `detail`, `instance`, and `type`.

## Technical Decisions
- **Error Handling**: Implemented **RFC 7807-compliant problem details** to standardize error responses.
- **Security**: Chose **JWT** for stateless authentication and role-based access control for simplicity and production readiness.
- **API Documentation**: Used **OpenAPI/Swagger** annotations to provide clear, interactive documentation for consumers.
- **CI/CD**: Chose **GitHub Actions** to automate builds and tests, enforcing quality and continuous integration.


## Learning Outcomes
- **Professional Error Handling**: Learned to use **RFC 7807** (“Problem Details for HTTP APIs”) to return structured, informative error responses, improving API robustness and client usability.
- **JWT Authentication**: Gained hands-on experience configuring **JWT-based authentication** and role-based access control in Spring Security.
- **Swagger/OpenAPI Documentation**: Reinforced previous experience in documenting APIs; applied best practices for endpoint descriptions, status codes, and error scenarios.
- **CI/CD Workflow Integration**: With this being the third workflow integration, further strengthened practical experience in setting up GitHub Actions to automatically build and test the project.
- **Kotlin Spring Boot Development**: Improved understanding of project structuring, dependency management, and **RESTful API design** using Kotlin and Spring Boot.

## AI Disclosure
### AI Tools Used
- ChatGPT (GPT-5 mini) for code comment improvement and guidance.

### AI-Assisted Work
- Suggested improved KDoc comments for exception handlers.
- Provided advice for structured error handling and API documentation.
- Assisted in configuring JWT authentication and authorization.
- Generated parts of the project report documentation.
- Estimated 30% AI-assisted code/comments.

### Original Work
- Implemented tests, endpoints, security, exception handling, Swagger documentation, and CI/CD manually.
- Verified and adapted AI suggestions to match project requirements and coding style.
