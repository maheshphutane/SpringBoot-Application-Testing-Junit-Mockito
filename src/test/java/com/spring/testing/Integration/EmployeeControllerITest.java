package com.spring.testing.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.testing.model.Employee;
import com.spring.testing.repository.EmployeeRepository;
import com.spring.testing.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;
    @BeforeEach
    public void setUp(){
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Narendra")
                .lastName("Modi")
                .email("pm.modi@gmail.com")
                .build();
    }
    @Autowired
    private EmployeeService employeeService;

    //Junit test for createEmployee
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSaved() throws Exception {
        //given - precondition or setup
        //given employee object in setup methode

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employee/saveEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect((MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail()))));

    }

    //Junit test for getAllEmployees
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfAllEmployeeObject() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(Employee.builder()
                .firstName("Amit")
                .lastName("Shah")
                .email("amit.shah@gmail.com")
                .build());

        for(Employee emp:employeeList){
            employeeRepository.save(emp);
        }

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/getAllEmployees"));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(2)));

    }

    //Junit test for getEmployeeById
    @Test
    public void givenEmployeeID_whenGetEmployeeByID_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        employeeRepository.save(employee);
        long employeeId = employee.getEmp_id();
        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/{id}",employeeId));

        //then - verify the output

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())));
    }

    //Junit test for getEmployeeById Negative testcase
    @Test
    public void givenInvalidEmployeeID_whenGetEmployeeByID_thenReturnEmpty() throws Exception {
        //given - precondition or setup

        employeeRepository.save(employee);
        long employeeId = 14L;
        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/{id}",employeeId));

        //then - verify the output

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    //Junit test for updateEmployee - positive scenario
    @DisplayName("updateEmployee Junit test")
    @Test
    public void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup

        Employee updatedEmp = Employee.builder()
                .firstName("Raja")
                .lastName("babu")
                .email("raja.babu@gmail.com")
                .build();
        employeeRepository.save(employee);
        long employee_id = employee.getEmp_id();

        //when - action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employee/{id}",employee_id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updatedEmp.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmp.getLastName())));

    }

    //Junit test for updateEmployee - negative scenario
    @DisplayName("updateEmployee Junit test")
    @Test
    public void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnStatusCode404() throws Exception {
        //given - precondition or setup


        Employee updatedEmp = Employee.builder()
                .firstName("Raja")
                .lastName("babu")
                .email("raja.babu@gmail.com")
                .build();
        long employee_id = 1;



        //when - action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employee/{id}",employee_id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    //Junit test for deleteEmployeeById
    @Test
    public void givenEmployeeId_whenDeleteEmployeeById_thenStatusMsgOk() throws Exception {
        //given - precondition or setup

        employeeRepository.save(employee);
        long employee_id = employee.getEmp_id();

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/{id}",employee_id));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
