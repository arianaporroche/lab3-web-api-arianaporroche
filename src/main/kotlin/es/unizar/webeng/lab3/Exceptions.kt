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
}
