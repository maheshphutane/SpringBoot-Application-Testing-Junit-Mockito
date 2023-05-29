package com.spring.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.testing.model.Employee;
import com.spring.testing.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;
    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Narendra")
                .lastName("Modi")
                .email("pm.modi@gmail.com")
                .build();
    }

    //Junit test for createEmployee
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSaved() throws Exception {
        //given - precondition or setup
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

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
}
