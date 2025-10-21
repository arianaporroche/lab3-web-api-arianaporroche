package es.unizar.webeng.lab3

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {
    @Autowired
    lateinit var mvc: MockMvc

    private fun loginRequest(
        username: String,
        password: String,
    ) = """
        { "username": "$username", "password": "$password" }
        """.trimIndent()

    @Test
    fun `login should succeed for admin`() {
        val result =
            mvc
                .post("/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = loginRequest("admin", "1234")
                }.andReturn()
                .response

        assertTrue(result.status == 200, "Admin login should return 200")
        assertTrue(result.contentAsString.contains("token"), "Response should contain JWT token")
    }

    @Test
    fun `login should succeed for user`() {
        val result =
            mvc
                .post("/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = loginRequest("user", "1234")
                }.andReturn()
                .response

        assertTrue(result.status == 200, "User login should return 200")
        assertTrue(result.contentAsString.contains("token"), "Response should contain JWT token")
    }

    @Test
    fun `login should fail with invalid credentials`() {
        val result =
            mvc
                .post("/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = loginRequest("wrong", "wrong")
                }.andReturn()
                .response

        assertTrue(result.status == 401, "Invalid credentials should return 401")
    }
}
