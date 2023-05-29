package com.spring.testing.service;

import com.spring.testing.exception.ResourceNotFoundException;
import com.spring.testing.model.Employee;
import com.spring.testing.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {


    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee){
        Optional<Employee> savedEmp = employeeRepository.findById(employee.getEmp_id());
        if(savedEmp.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with given mail: "+employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(long id){
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(long id){
        employeeRepository.deleteById(id);
        return;
    }
}
