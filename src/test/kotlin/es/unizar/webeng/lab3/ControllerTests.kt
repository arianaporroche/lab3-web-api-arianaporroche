package es.unizar.webeng.lab3

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.util.Optional

private val MANAGER_REQUEST_BODY = { name: String ->
    """
    { 
        "role": "Manager", 
        "name": "$name" 
    }
    """
}

private val MANAGER_RESPONSE_BODY = { name: String, id: Int ->
    """
    { 
       "name" : "$name",
       "role" : "Manager",
       "id" : $id
    }
    """
}

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ControllerTests {
    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var employeeRepository: EmployeeRepository

    private lateinit var jwtToken: String

    @BeforeEach
    fun obtainToken() {
        val loginRequest = """
            { "username": "admin", "password": "1234" }
        """
        val response =
            mvc
                .post("/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = loginRequest
                }.andReturn()
                .response
                .contentAsString

        // Parse JSON { "token": "..." }
        jwtToken = """$response""".substringAfter("\"token\":\"").substringBefore("\"")
    }

    private fun authHeader() = "Bearer $jwtToken"

    @Test
    fun `post is not safe and not idempotent`() {
        // SETUP
        every {
            employeeRepository.save(any<Employee>())
        } answers {
            Employee("Mary", "Manager", 1)
        } andThenAnswer {
            Employee("Mary", "Manager", 2)
        }
        //

        mvc
            .post("/employees") {
                contentType = MediaType.APPLICATION_JSON
                content = MANAGER_REQUEST_BODY("Mary")
                accept = MediaType.APPLICATION_JSON
                header("Authorization", authHeader())
            }.andExpect {
                status { isCreated() }
                header { string("Location", "http://localhost/employees/1") }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(MANAGER_RESPONSE_BODY("Mary", 1))
                }
            }

        mvc
            .post("/employees") {
                contentType = MediaType.APPLICATION_JSON
                content = MANAGER_REQUEST_BODY("Mary")
                accept = MediaType.APPLICATION_JSON
                header("Authorization", authHeader())
            }.andExpect {
                status { isCreated() }
                header { string("Location", "http://localhost/employees/2") }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(MANAGER_RESPONSE_BODY("Mary", 2))
                }
            }

        // VERIFY
        verify(exactly = 2) {
            employeeRepository.save(any<Employee>())
        }
        //
    }

    @Test
    fun `get is safe and idempotent`() {
        // SETUP
        every {
            employeeRepository.findById(1)
        } answers {
            Optional.of(Employee("Mary", "Manager", 1))
        }

        every {
            employeeRepository.findById(2)
        } answers {
            Optional.empty()
        }
        //

        mvc
            .get("/employees/1") {
                header("Authorization", authHeader())
            }.andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(MANAGER_RESPONSE_BODY("Mary", 1))
                }
            }

        mvc
            .get("/employees/1") {
                header("Authorization", authHeader())
            }.andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(MANAGER_RESPONSE_BODY("Mary", 1))
                }
            }

        mvc
            .get("/employees/2") {
                header("Authorization", authHeader())
            }.andExpect {
                status { isNotFound() }
            }

        // VERIFY
        verify(exactly = 0) {
            employeeRepository.save(any<Employee>())
            employeeRepository.deleteById(any())
            employeeRepository.findAll()
        }
        //
    }

    @Test
    fun `put is idempotent but not safe`() {
        // SETUP
        every {
            employeeRepository.findById(1)
        } answers {
            Optional.empty()
        } andThenAnswer {
            Optional.of(Employee("Tom", "Manager", 1))
        }

        every {
            employeeRepository.save(any<Employee>())
        } answers {
            Employee("Tom", "Manager", 1)
        }
        //

        mvc
            .put("/employees/1") {
                contentType = MediaType.APPLICATION_JSON
                content = MANAGER_REQUEST_BODY("Tom")
                accept = MediaType.APPLICATION_JSON
                header("Authorization", authHeader())
            }.andExpect {
                status { isCreated() }
                header { string("Content-Location", "http://localhost/employees/1") }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(MANAGER_RESPONSE_BODY("Tom", 1))
                }
            }

        mvc
            .put("/employees/1") {
                contentType = MediaType.APPLICATION_JSON
                content = MANAGER_REQUEST_BODY("Tom")
                accept = MediaType.APPLICATION_JSON
                header("Authorization", authHeader())
            }.andExpect {
                status { isOk() }
                header { string("Content-Location", "http://localhost/employees/1") }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(MANAGER_RESPONSE_BODY("Tom", 1))
                }
            }

        // VERIFY
        verify(exactly = 2) {
            employeeRepository.save(any<Employee>())
            employeeRepository.findById(1)
        }
        verify(exactly = 0) {
            employeeRepository.findAll()
            employeeRepository.deleteById(any())
        }
        //
    }

    @Test
    fun `delete is idempotent but not safe`() {
        // SETUP
        justRun {
            employeeRepository.deleteById(1)
        }
        //

        mvc
            .delete("/employees/1") {
                header("Authorization", authHeader())
            }.andExpect {
                status { isNoContent() }
            }

        mvc
            .delete("/employees/1") {
                header("Authorization", authHeader())
            }.andExpect {
                status { isNoContent() }
            }

        // VERIFY
        verify(exactly = 2) {
            employeeRepository.deleteById(1)
        }
        verify(exactly = 0) {
            employeeRepository.save(any<Employee>())
            employeeRepository.findById(any())
            employeeRepository.findAll()
        }
        //
    }
}
