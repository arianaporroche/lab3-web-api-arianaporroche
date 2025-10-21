package es.unizar.webeng.lab3

/**
 * Container class for all custom exceptions in the application.
 *
 * Each inner class represents a specific error scenario that can occur in the system.
 */
class Exceptions {
    /**
     * Thrown when an employee with the specified ID cannot be found in the database.
     *
     * @param id The ID of the employee that was not found.
     */
    class EmployeeNotFoundException(
        id: Long,
    ) : RuntimeException("Could not find employee $id")

    /**
     * Thrown when attempting to create a new employee with an ID that already exists in the repository.
     *
     * This prevents duplication of employee records and enforces uniqueness of IDs.
     *
     * @param id The ID of the employee that already exists.
     */
    class EmployeeAlreadyExistsException(
        id: Long,
    ) : RuntimeException("Employee with id $id already exists")

    /**
     * Thrown when a request to retrieve employees returns an empty list.
     *
     * This indicates that there are currently no employees in the repository.
     * Clients can use this information to handle empty data scenarios gracefully.
     */
    class NoEmployeesFoundException : RuntimeException("No employees found")
}
