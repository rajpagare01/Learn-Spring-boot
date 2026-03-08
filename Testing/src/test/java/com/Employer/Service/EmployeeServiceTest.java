package com.Employer.Service;

import com.Employer.Model.Employee;
import com.Employer.Repo.EmployeeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepo repo;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Raj");
        employee.setLastName("Pagare");
        employee.setEmail("raj@gmail.com");
    }

    @Test
    void testCreateEmployee() {
        when(repo.save(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeService.createEmployee(employee);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(employee, response.getBody());

        verify(repo, times(1)).save(employee);
    }

    @Test
    void testGetAllEmployee() {

        List<Employee> list = Arrays.asList(employee);

        when(repo.findAll()).thenReturn(list);

        ResponseEntity<?> response = employeeService.getAllEmployee();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(list, response.getBody());
    }

    @Test
    void testGetEmployeeById()
    {
        when(repo.findById(1)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response =employeeService.getEmployeeById(1);

        assertEquals(200 , response.getStatusCode().value());
        assertEquals(employee , response.getBody());
    }
    @Test
    void testUpdateEmployee() {

        Employee updated = new Employee();
        updated.setFirstName("Jay");
        updated.setLastName("Pagare");
        updated.setEmail("jay@123");

        when(repo.findById(1)).thenReturn(Optional.of(employee));
        when(repo.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<Employee> response =
                employeeService.updateEmployee(1, updated);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(employee, response.getBody());

        verify(repo, times(1)).save(any(Employee.class));
    }


    @Test
    void testDeleteEmployee() {

        when(repo.findById(1)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response = employeeService.deleteEmployee(1);

        assertEquals(200, response.getStatusCode().value());

        verify(repo, times(1)).delete(employee);
    }
}