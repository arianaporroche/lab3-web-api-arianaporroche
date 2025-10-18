package es.unizar.webeng.lab3

import es.unizar.webeng.lab3.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(
    val username: String,
    val password: String,
)

data class TokenResponse(
    val token: String,
)

@RestController
class AuthController {
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<TokenResponse> {
        // Aquí podrías validar contra una base de datos, pero haremos algo básico:
        return if (request.username == "admin" && request.password == "1234") {
            val token = JwtUtil.generateToken(request.username)
            ResponseEntity.ok(TokenResponse(token))
        } else {
            ResponseEntity.status(401).build()
        }
    }
}
