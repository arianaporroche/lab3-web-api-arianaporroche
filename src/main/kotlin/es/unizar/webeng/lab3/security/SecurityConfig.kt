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
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // Público
                    .requestMatchers("/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    // GET (lectura) accesible a USER y ADMIN
                    .requestMatchers(HttpMethod.GET, "/employees/**")
                    .hasAnyRole("USER", "ADMIN")
                    // POST, PUT, DELETE (modificación) solo para ADMIN
                    .requestMatchers(HttpMethod.POST, "/employees/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/employees/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/employees/**")
                    .hasRole("ADMIN")
                    // Cualquier otra ruta → autenticada
                    .anyRequest()
                    .authenticated()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        // Insertar el filtro JWT antes del de autenticación por usuario/contraseña
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
