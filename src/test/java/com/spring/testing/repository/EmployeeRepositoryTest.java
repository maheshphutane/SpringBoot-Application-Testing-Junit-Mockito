package com.spring.testing.repository;

import com.spring.testing.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
public class EmployeeRepositoryTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Mahesh")
                .lastName("Phutane")
                .email("mahesh.phutane@tcs.com")
                .build();
    }

    //Junit test for save employee details operation
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployeeObject(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Mahesh")
//                .lastName("Phutane")
//                .email("mahesh.phutane@tcs.com")
//                .build();

        //when - action or behaviour we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Mahesh");
    }

    //Junit test for getting all employees operation
    @Test
    public void givenEmployeeList_whenFindALL_thenReturnAllEmployees(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Mahesh")
//                .lastName("Phutane")
//                .email("mahesh.phutane@tcs.com")
//                .build();

        Employee employee1 = Employee.builder()
                .firstName("Vinayak")
                .lastName("Waghmode")
                .email("vinayak.mode@tcs.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action or behaviour we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList).isNotNull();
    }

    //Junit test for
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Mahesh")
//                .lastName("Phutane")
//                .email("mahesh.phutane@tcs.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        Employee employee1 = employeeRepository.findById(employee.getEmp_id()).get();

        //then - verify the output
        assertThat(employee1).isNotNull();
        assertThat(employee1.getEmp_id()).isEqualTo(employee.getEmp_id());
    }

    //Junit test for Delete Employee by Id operation
    @Test
    public void givenEmployeeId_whenDeleteById_thenReturnNull(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Mahesh")
//                .lastName("Phutane")
//                .email("mahesh.phutane@tcs.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
         employeeRepository.deleteById(employee.getEmp_id());
        //then - verify the output
        Optional<Employee> isDeleted = employeeRepository.findById(employee.getEmp_id());
        assertThat(isDeleted).isEmpty();
    }

    //Junit test for Custom JPQL query (find by first_name and last_name)
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQA_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Mahesh")
//                .lastName("Phutane")
//                .email("mahesh.phutane@tcs.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we are going to test
        String firstName = "Mahesh";
        String lastName = "Phutane";

        Employee employee1 = employeeRepository.findJPQL(firstName,lastName);


        //then - verify the output
        assertThat(employee1).isNotNull();
        assertThat(employee1.getFirstName()).isEqualTo("Mahesh");
        assertThat(employee1.getLastName()).isEqualTo("Phutane");
    }
}
