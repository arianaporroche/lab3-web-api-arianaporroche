package es.unizar.webeng.lab3.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date

object JwtUtil {
    // Secret key for signing JWTs (read from environment variable or use default for development)
    private val SECRET = System.getenv("JWT_SECRET") ?: "default_secret_dev"

    // Token expiration time in milliseconds (1 hour)
    private const val EXPIRATION_TIME = 1000 * 60 * 60

    // Algorithm used to sign the JWT
    private val algorithm = Algorithm.HMAC256(SECRET)

    /**
     * Generates a JWT for the given username and role
     * @param username the user identifier
     * @param role the user's role (e.g., USER or ADMIN)
     * @return signed JWT string
     */
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

    /**
     * Validates a JWT and returns the decoded token if valid
     * @param token the JWT string to validate
     * @return DecodedJWT if valid, or null if invalid/expired
     */
    fun validateToken(token: String): DecodedJWT? =
        try {
            JWT.require(algorithm).build().verify(token)
        } catch (e: Exception) {
            null
        }
}
