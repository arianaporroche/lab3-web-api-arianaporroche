package es.unizar.webeng.lab3

import es.unizar.webeng.lab3.Exceptions.EmployeeNotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@Tag(name = "Employees", description = "API para gestionar empleados")
@RestController
@SecurityRequirement(name = "bearerAuth")
class EmployeeController(
    private val repository: EmployeeRepository,
) {
    @Operation(summary = "Obtener todos los empleados", description = "Devuelve la lista completa de empleados")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        ],
    )
    @GetMapping("/employees")
    fun all(): Iterable<Employee> = repository.findAll()

    @Operation(summary = "Crear un nuevo empleado", description = "Crea un empleado y devuelve su información con ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Empleado creado correctamente"),
            ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        ],
    )
    @PostMapping("/employees")
    fun newEmployee(
        @RequestBody newEmployee: Employee,
    ): ResponseEntity<Employee> {
        val employee = repository.save(newEmployee)
        val location =
            ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/employees/{id}")
                .build(employee.id)
        return ResponseEntity.created(location).body(employee)
    }

    @Operation(summary = "Obtener un empleado por ID", description = "Devuelve un empleado específico por su ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Empleado encontrado"),
            ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        ],
    )
    @GetMapping("/employees/{id}")
    fun one(
        @PathVariable id: Long,
    ): Employee = repository.findById(id).orElseThrow { EmployeeNotFoundException(id) }

    @Operation(summary = "Actualizar o crear un empleado por ID", description = "Actualiza un empleado existente o lo crea si no existe")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Empleado actualizado"),
            ApiResponse(responseCode = "201", description = "Empleado creado"),
        ],
    )
    @PutMapping("/employees/{id}")
    fun replaceEmployee(
        @RequestBody newEmployee: Employee,
        @PathVariable id: Long,
    ): ResponseEntity<Employee> {
        val location =
            ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/employees/{id}")
                .build(id)
                .toASCIIString()
        val (status, body) =
            repository
                .findById(id)
                .map { employee ->
                    employee.name = newEmployee.name
                    employee.role = newEmployee.role
                    repository.save(employee)
                    HttpStatus.OK to employee
                }.orElseGet {
                    newEmployee.id = id
                    repository.save(newEmployee)
                    HttpStatus.CREATED to newEmployee
                }
        return ResponseEntity.status(status).header("Content-Location", location).body(body)
    }

    @Operation(summary = "Borrar un empleado por ID", description = "Elimina un empleado existente por su ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Empleado eliminado correctamente"),
            ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
        ],
    )
    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        repository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Handles EmployeeNotFoundException and returns an RFC 7807–compliant error response.
     *
     * This method converts the thrown [EmployeeNotFoundException] into a standardized
     * [ProblemDetail] object according to RFC 7807 ("Problem Details for HTTP APIs").
     * It provides structured information about the error, making it easier for clients
     * to understand and handle API problems consistently.
     *
     * @param ex The exception that was thrown when an employee was not found.
     * @param request The HTTP request that triggered the exception.
     * @return A [ResponseEntity] containing a [ProblemDetail] with status 404 (Not Found).
     */
    @ExceptionHandler(EmployeeNotFoundException::class)
    fun handleNotFound(
        ex: EmployeeNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND)
        problem.title = "Resource not found"
        problem.detail = ex.message
        problem.instance = URI.create(request.requestURI)
        problem.type = URI.create("https://example.com/problem/not-found")

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem)
    }
}
