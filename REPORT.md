# Lab 3 Complete a Web API -- Project Report

## Description of Changes
[Detailed description of all changes made]


Bonus:
2. **Implement RESTful API Documentation with OpenAPI/Swagger**
http://localhost:8080/swagger-ui/index.html
- OpenAPI Documentation: Annotated the controller with OpenAPI/Swagger annotations to generate interactive API documentation.


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
- EmployeeController: Implemented endpoints for CRUD operations on employees (`GET`, `POST`, `PUT`, `DELETE`) with appropriate access control.
- JWT Utilities: Created `JwtUtil` to generate and validate JWT tokens.
- JWT Filter: Added `JwtFilter` to process incoming requests, validate JWTs, and populate the SecurityContext with authenticated user details.



## Technical Decisions
[Explanation of technical choices made]

Bonus:
2. **Implement RESTful API Documentation with OpenAPI/Swagger**



1. **Security and Authentication and Authorization**
- Used Spring Security with a stateless session policy for token-based authentication.  
- Chose JWT as the authentication mechanism for simplicity and statelessness.  
- Role-based access control ensures only authorized users can modify data.  
- Swagger/OpenAPI used to provide interactive API documentation and facilitate testing.

## Learning Outcomes
[What you learned from this assignment]

## AI Disclosure
### AI Tools Used
- [List specific AI tools used]

### AI-Assisted Work
- [Describe what was generated with AI assistance]
- [Percentage of AI-assisted vs. original work]
- [Any modifications made to AI-generated code]

### Original Work
- [Describe work done without AI assistance]
- [Your understanding and learning process]