package com.spring.testing.service;

import com.spring.testing.exception.ResourceNotFoundException;
import com.spring.testing.model.Employee;
import com.spring.testing.repository.EmployeeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Employee employee1;
    @BeforeEach
    public void setup(){

//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeService(employeeRepository);
        employee = Employee.builder()
                .firstName("xyz")
                .lastName("lmn")
                .email("xyz.lmn@abc.com")
                .build();

        employee1 = Employee.builder()
                .firstName("aaa")
                .lastName("bbb")
                .email("aaa.bbb@ccc.com")
                .build();
    }

    //Junit test for saveEmployee methode
    @DisplayName("Junit Test for saveEmployee methode ")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given - precondition or setup


        BDDMockito.given(employeeRepository.findById(employee.getEmp_id())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour we are going to test

        Employee employee1 = employeeService.saveEmployee(employee);
        //then - verify the output

        assertThat(employee1).isNotNull();
    }

    @DisplayName("Junit Test for saveEmployee methode in service class when exception occurs")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenThrowsException(){
        //given - precondition or setup


        BDDMockito.given(employeeRepository.findById(employee.getEmp_id())).willReturn(Optional.of(employee));
        //BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->
                employeeService.saveEmployee(employee));

        //then - verify the output
        Mockito.verify(employeeRepository,never()).save(Mockito.any(Employee.class));
    }

    //Junit test for getAllEmployee
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnListOfEmployeeObject(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
        //when - action or behaviour we are going to test
        List<Employee> employeeList = employeeService.getAllEmployee();
        //then - verify the output
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit test for getAllEmployee with negative test case
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyList(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //when - action or behaviour we are going to test
        List<Employee> employeeList = employeeService.getAllEmployee();
        //then - verify the output
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //Junit test for getEmployeeByID
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getEmp_id())).willReturn(Optional.of(employee));

        //when - action or behaviour we are going to test
        Employee employee2 = employeeService.getEmployeeById(employee.getEmp_id()).get();

        //then - verify the output
        assertThat(employee2).isNotNull();
        assertThat(employee2.getFirstName()).isEqualTo("xyz");
    }

    //Junit test for updateEmployee methode
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Tatya Vinchu");
        //when - action or behaviour we are going to test
        Employee emp = employeeService.updateEmployee(employee);
        //then - verify the output

        assertThat(emp.getFirstName()).isEqualTo("Tatya Vinchu");
    }

    //Junit test for deleteEmployee
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenDeleteTheEmployeeObject(){
        //given - precondition or setup
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(employee.getEmp_id());

        //when - action or behaviour we are going to test
        employeeService.deleteEmployee(employee.getEmp_id());
        //then - verify the output
        Mockito.verify(employeeRepository,Mockito.times(1)).deleteById(employee.getEmp_id());
    }
}
