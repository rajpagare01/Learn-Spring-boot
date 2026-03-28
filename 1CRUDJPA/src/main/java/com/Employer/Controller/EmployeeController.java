package com.Employer.Controller;

import com.Employer.Model.Employee;
import com.Employer.Service.EmployeeService;
import com.Employer.Service.WeatherService;
import com.Employer.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping("/employee")
    public ResponseEntity<?> getAllEmployee() {
        return employeeService.getAllEmployee();
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }


    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/{city}")
    public ResponseEntity<?> exgreeting(@PathVariable String city) {
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        String greet = "";
        if(weatherResponse != null)
        {
            greet = "weather feels like"+weatherResponse.getCurrent().getFeelslike();
        }
      return new ResponseEntity<>("hi" +greet, HttpStatus.OK);
    }
    }