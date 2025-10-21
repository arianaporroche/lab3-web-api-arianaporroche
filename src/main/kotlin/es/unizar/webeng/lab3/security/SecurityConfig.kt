package es.unizar.webeng.lab3.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtFilter: JwtFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Disable CSRF since we are using JWTs
            .authorizeHttpRequests { auth ->
                auth
                    // Public endpoints
                    .requestMatchers("/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    // GET accessible by USER and ADMIN
                    .requestMatchers(HttpMethod.GET, "/employees/**")
                    .hasAnyRole("USER", "ADMIN")
                    // POST, PUT, DELETE accessible only by ADMIN
                    .requestMatchers(HttpMethod.POST, "/employees/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/employees/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/employees/**")
                    .hasRole("ADMIN")
                    // Any other request must be authenticated
                    .anyRequest()
                    .authenticated()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless session; no server-side session
            }

        // Add JWT filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
