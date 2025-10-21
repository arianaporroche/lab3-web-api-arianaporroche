package es.unizar.webeng.lab3

/** Contenedor de todas las excepciones de la aplicación */
class Exceptions {
    class EmployeeNotFoundException(
        id: Long,
    ) : RuntimeException("Could not find employee $id")

    // Aquí puedes añadir más excepciones personalizadas
}

// package es.unizar.webeng.lab3

// import org.springframework.http.HttpStatus
// import org.springframework.web.bind.annotation.ResponseStatus

// /** Contenedor de todas las excepciones de la aplicación */
// class Exceptions {

//     @ResponseStatus(HttpStatus.NOT_FOUND)
//     class EmployeeNotFoundException(id: Long) :
//         RuntimeException("Could not find employee $id")

//     // Aquí puedes añadir más excepciones personalizadas
// }
