package com.Employer.Service;

import com.Employer.Model.Employee;
import com.Employer.Repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService {


    private  EmployeeRepo empRepo;
    EmployeeService(EmployeeRepo empRepo) {
        this.empRepo = empRepo;
    }


    public ResponseEntity<Employee> createEmployee(Employee employee) {
        Employee emp = empRepo.save(employee);
        if (emp == null) {
            return ResponseEntity.status(500).build();
        } else {
            return ResponseEntity.ok(emp);
        }
    }

    public ResponseEntity<?> getAllEmployee() {
        List<Employee> empList = empRepo.findAll();
        if (empList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(empList);
        }
    }

    public ResponseEntity<?> getEmployeeById(int id) {
        Employee emp  =  empRepo.findById(id).orElse(null);
        if (emp == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(emp);
        }
    }

    public ResponseEntity<Employee> updateEmployee(int id, Employee employee) {
        Employee emp = empRepo.findById(id).orElse(null);
        if (emp == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            emp.setFirstName(employee.getFirstName());
            emp.setLastName(employee.getLastName());
            emp.setEmail(employee.getEmail());
            Employee updatedEmp = empRepo.save(emp);
            return ResponseEntity.ok(updatedEmp);
        }

    }

    public ResponseEntity<?> deleteEmployee(int id) {
        Employee emp = empRepo.findById(id).orElse(null);
        if (emp == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            empRepo.delete(emp);
            return ResponseEntity.ok().build();
        }
    }
}

