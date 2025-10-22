package es.unizar.webeng.lab3

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
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

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerSecurityTests {
    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var employeeRepository: EmployeeRepository

    // ======================================================
    // Tests for user with role "USER"
    // ======================================================

    /** Test that a USER can get an employee by ID */
    @Test
    @WithMockUser(username = "user", roles = ["USER"])
    fun `get by id is accessible for USER`() {
        // SETUP
        every {
            employeeRepository.findById(1)
        } answers {
            Optional.of(Employee("Mary", "Manager", 1))
        }
        //

        mvc
            .get("/employees/1")
            .andExpect {
                status { isOk() }
            }
    }

    /** Test that a USER can get all employees */
    @Test
    @WithMockUser(username = "USER", roles = ["USER"])
    fun `get all is accessible for USER`() {
        // SETUP
        every { employeeRepository.findAll() } answers {
            listOf(Employee("Mary", "Manager", 1))
        }
        //

        mvc
            .get("/employees")
            .andExpect {
                status { isOk() }
            }
    }

    /** Test that a USER cannot post a new employee */
    @Test
    @WithMockUser(username = "USER", roles = ["USER"])
    fun `post is not accessible for USER`() {
        mvc
            .post("/employees")
            .andExpect {
                status { isForbidden() }
            }
    }

    /** Test that a USER cannot update an employee */
    @Test
    @WithMockUser(username = "user", roles = ["USER"])
    fun `put is not accessible for USER`() {
        mvc
            .put("/employees/1")
            .andExpect {
                status { isForbidden() }
            }
    }

    /** Test that a USER cannot delete an employee */
    @Test
    @WithMockUser(username = "user", roles = ["USER"])
    fun `delete is not accessible for USER`() {
        mvc
            .delete("/employees/1")
            .andExpect {
                status { isForbidden() }
            }
    }

    // ======================================================
    // Tests for admin with role "ADMIN"
    // ======================================================

    /** Test that an ADMIN can get an employee by ID */
    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `get by id is accessible for ADMIN`() {
        // SETUP
        every {
            employeeRepository.findById(1)
        } answers {
            Optional.of(Employee("Mary", "Manager", 1))
        }
        //

        mvc
            .get("/employees/1")
            .andExpect {
                status { isOk() }
            }
    }

    /** Test that an ADMIN can get all employees */
    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `get all is accessible for ADMIN`() {
        // SETUP
        every { employeeRepository.findAll() } answers {
            listOf(Employee("Mary", "Manager", 1))
        }
        //

        mvc
            .get("/employees")
            .andExpect {
                status { isOk() }
            }
    }

    /** Test that an ADMIN can post a new employee */
    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `post is accessible for ADMIN`() {
        // SETUP
        every {
            employeeRepository.save(any<Employee>())
        } answers {
            Employee("Mary", "Manager", 1)
        }
        //

        mvc
            .post("/employees") {
                contentType = MediaType.APPLICATION_JSON
                content = MANAGER_REQUEST_BODY("Mary")
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isCreated() }
            }
    }

    /** Test that an ADMIN can update an employee */
    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `put is accessible for ADMIN`() {
        // SETUP
        every {
            employeeRepository.findById(1)
        } answers {
            Optional.empty()
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
            }.andExpect {
                status { isCreated() }
            }
    }

    /** Test that an ADMIN can delete an employee */
    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun `delete is accessible for ADMIN`() {
        // SETUP
        justRun {
            employeeRepository.deleteById(1)
        }
        //

        mvc
            .delete("/employees/1")
            .andExpect {
                status { isNoContent() }
            }
    }
}
