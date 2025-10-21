package es.unizar.webeng.lab3.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

object JwtUtil {
    private val SECRET = System.getenv("JWT_SECRET") ?: "default_secret_dev"
    private const val EXPIRATION_TIME = 1000 * 60 * 60 // 1 hora
    private val algorithm = Algorithm.HMAC256(SECRET)

    fun generateToken(
        username: String,
        role: String,
    ): String =
        JWT
            .create()
            .withSubject(username)
            .withClaim("role", role)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)

    fun validateToken(token: String): DecodedJWT? =
        try {
            JWT.require(algorithm).build().verify(token)
        } catch (e: Exception) {
            null
        }
}
