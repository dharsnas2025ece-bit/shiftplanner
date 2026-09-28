package com.example.shiftplanner.service;

import com.example.shiftplanner.entity.Employee;
import com.example.shiftplanner.repository.EmployeeRepository;
import com.example.shiftplanner.repository.RosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RosterRepository rosterRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, Employee employee) {

        Optional<Employee> existingEmployee =
                employeeRepository.findById(id);

        if (existingEmployee.isPresent()) {

            Employee emp = existingEmployee.get();

            emp.setName(employee.getName());
            emp.setEmail(employee.getEmail());
            emp.setRole(employee.getRole());

            return employeeRepository.save(emp);
        }

        return null;
    }

    public void deleteEmployee(Long id) {

        List<com.example.shiftplanner.entity.Roster> rosters =
                rosterRepository.findAll();

        for (com.example.shiftplanner.entity.Roster roster : rosters) {

            if (roster.getEmployee() != null &&
                    roster.getEmployee().getId().equals(id)) {

                rosterRepository.deleteById(roster.getId());
            }
        }

        employeeRepository.deleteById(id);
    }
}