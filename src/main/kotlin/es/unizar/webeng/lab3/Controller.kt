package es.unizar.webeng.lab3

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Tag(name = "Employees", description = "API para gestionar empleados")
@RestController
class EmployeeController(
    private val repository: EmployeeRepository,
) {
    @Operation(summary = "Obtener todos los empleados", description = "Devuelve la lista completa de empleados")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
        ]
    )
    @GetMapping("/employees")
    fun all(): Iterable<Employee> = repository.findAll()

    @Operation(summary = "Crear un nuevo empleado", description = "Crea un empleado y devuelve su información con ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Empleado creado correctamente"),
            ApiResponse(responseCode = "400", description = "Solicitud inválida")
        ]
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
            ApiResponse(responseCode = "404", description = "Empleado no encontrado")
        ]
    )
    @GetMapping("/employees/{id}")
    fun one(
        @PathVariable id: Long,
    ): Employee = repository.findById(id).orElseThrow { EmployeeNotFoundException(id) }

    @Operation(summary = "Actualizar o crear un empleado por ID", description = "Actualiza un empleado existente o lo crea si no existe")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Empleado actualizado"),
            ApiResponse(responseCode = "201", description = "Empleado creado")
        ]
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
            ApiResponse(responseCode = "404", description = "Empleado no encontrado")
        ]
    )
    @DeleteMapping("/employees/{id}")
    fun deleteEmployee(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        repository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class EmployeeNotFoundException(
    id: Long,
) : Exception("Could not find employee $id")
