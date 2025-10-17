package es.unizar.webeng.lab3

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class SwaggerDocumentationTests {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `Swagger JSON should contain employees endpoints`() {
        val result =
            mockMvc
                .get("/v3/api-docs")
                .andExpect { status { isOk() } }
                .andReturn()
                .response
                .contentAsString

        // Verifica que los endpoints principales aparecen en el JSON
        assertTrue(result.contains("/employees"), "Debe contener /employees")
        assertTrue(result.contains("/employees/{id}"), "Debe contener /employees/{id}")

        // Verifica que POST aparece
        assertTrue(result.contains("\"post\""), "Debe contener POST en /employees")

        // Verifica que GET aparece
        assertTrue(result.contains("\"get\""), "Debe contener GET en /employees y /employees/{id}")

        // Verifica que DELETE aparece
        assertTrue(result.contains("\"delete\""), "Debe contener DELETE en /employees/{id}")

        // Verifica que PUT aparece
        assertTrue(result.contains("\"put\""), "Debe contener PUT en /employees/{id}")
    }
}
