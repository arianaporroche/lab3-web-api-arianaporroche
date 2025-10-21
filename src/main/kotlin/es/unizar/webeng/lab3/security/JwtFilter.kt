package es.unizar.webeng.lab3.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter : OncePerRequestFilter() {

    /**
     * Filter method executed once per request.
     * It checks if the Authorization header contains a valid JWT
     * and sets the user authentication in the SecurityContext if valid.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // Read the Authorization header from the request
        val header = request.getHeader("Authorization")

        // Check if the header contains a Bearer token
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            val jwt = JwtUtil.validateToken(token)
            if (jwt != null) {
                val username = jwt.subject
                val role = jwt.getClaim("role").asString()
                val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))

                // Create an Authentication object with username and roles
                val authentication =
                    UsernamePasswordAuthenticationToken(
                        User(username, "", authorities),
                        null,
                        authorities,
                    )

                // Attach request details to authentication
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}
