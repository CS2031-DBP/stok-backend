package com.example.stokapp.employee.application;

import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.employee.domain.EmployeeService;
import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.user.domain.Role;
import com.example.stokapp.utils.Reader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "santiago.silva@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@WithMockUser(roles = "EMPLOYEE")
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    public void setUp() throws Exception {
        ownerRepository.deleteAll();

        // Registrar un nuevo propietario
        String jsonContent = Reader.readJsonFile("/employee/post.json");

        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        String token = jsonObject.getString("token");
        System.out.println("Token: " + token);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Long employeeIdToUpdate = employeeRepository
                .findByEmail("santiago.silva@utec.edu.pe")
                .orElseThrow()
                .getId();

        Owner owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        owner.setRole(Role.OWNER);
        ownerRepository.save(owner);

        Employee employee = employeeRepository.findById(employeeIdToUpdate).orElseThrow(null);
        employee.setOwner(owner);
        employeeRepository.save(employee);

        String updateContent = Reader.readJsonFile("/employee/update.json");

        mockMvc.perform(patch("/employees/update/{employeeId}", employeeIdToUpdate)
                        .contentType(APPLICATION_JSON)
                        .content(updateContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee updated"));
    }
}