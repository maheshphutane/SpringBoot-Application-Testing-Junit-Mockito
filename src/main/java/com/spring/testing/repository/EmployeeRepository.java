package com.spring.testing.repository;

import com.spring.testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //Define custom JPQL(Java Persistence Query Language)
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findJPQL(String firstName, String lastName);
}
