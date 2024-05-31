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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = "joel.miguel@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@WithMockUser(roles = "OWNER")
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EmployeeService employeeService;
    String token = "";

    @BeforeEach
    public void setUp() throws Exception {
        ownerRepository.deleteAll();

        // Registrar un nuevo propietario
        String jsonContent = Reader.readJsonFile("/owner/post.json");

        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        // Obtener el token del usuario registrado para usarlo en las solicitudes futuras
        String token = jsonObject.getString("token");
        System.out.println("Token: " + token);

    }


    @Test
    public void testAuthorizedAccessToGetEmployeeById() throws Exception {
        Employee employee = new Employee();
        employee.setEmail("Bellido20@gmail.com");
        employee.setFirstName("Jesus");
        employee.setLastName("Bellido");
        employee.setRole(Role.EMPLOYEE);
        employee.setPhoneNumber("992020202");
        employee.setCreatedAt(ZonedDateTime.now());
        employee.setPassword("c20a");

        Long ownerId = ownerRepository.findByEmail("santiago.silva@utec.edu.pe").orElseThrow().getId();
        employeeRepository.save(employee);

        employeeService.assignEmployeeToOwner(ownerId,employee.getId());

        employeeRepository.save(employee);

        mockMvc.perform(get("/employees/{ownerId}/{employeeId}", ownerId, employee.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "joel.miguel@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @WithMockUser(roles = "EMPLOYEE")
    @Test
    public void testDeleteEmployee() throws Exception {

        String token = "";

        employeeRepository.deleteAll();

        String jsonContent = Reader.readJsonFile("/employee/post.json");

        var res = mockMvc.perform(post("/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        token = jsonObject.getString("token");
        System.out.println("Token: " + token);

        Owner owner = new Owner();
        owner.setEmail("Bellido20@gmail.com");
        owner.setFirstName("Jesus");
        owner.setLastName("Bellido");
        owner.setRole(Role.OWNER);
        owner.setPhoneNumber("992020202");
        owner.setCreatedAt(ZonedDateTime.now());
        owner.setPassword("c20a");
        ownerRepository.save(owner);

        Long ownerId = owner.getId();
        Long employeeIdToDelete = employeeRepository.findByEmail("joel.miguel@utec.edu.pe").orElseThrow().getId();

        mockMvc.perform(delete("/employees/delete/{ownerId}/{employeeId}", ownerId, employeeIdToDelete)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
