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
    ): ResponseEntity<TokenResponse> =
        when {
            request.username == "admin" && request.password == "1234" -> {
                val token = JwtUtil.generateToken(request.username, "ADMIN")
                ResponseEntity.ok(TokenResponse(token))
            }
            request.username == "user" && request.password == "1234" -> {
                val token = JwtUtil.generateToken(request.username, "USER")
                ResponseEntity.ok(TokenResponse(token))
            }
            else -> ResponseEntity.status(401).build()
        }
}
