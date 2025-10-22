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
        val token =
            when {
                request.username == "admin" && request.password == "1234" -> JwtUtil.generateToken(request.username, "ADMIN")
                request.username == "user" && request.password == "1234" -> JwtUtil.generateToken(request.username, "USER")
                else -> null
            }

        return if (token != null) {
            ResponseEntity
                .ok()
                .header("Authorization", "Bearer $token")
                .body(TokenResponse(token))
        } else {
            ResponseEntity.status(401).build()
        }
    }
}
