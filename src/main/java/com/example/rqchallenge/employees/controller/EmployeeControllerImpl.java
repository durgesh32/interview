package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.EmployeeService;
import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class EmployeeControllerImpl implements IEmployeeController {

	private final EmployeeService employeeService;

	@Autowired
	public EmployeeControllerImpl(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Override
	@GetMapping()
	public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
		List<Employee> employeeList = employeeService.getEmployees();
		if (employeeList == null) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(employeeList, HttpStatus.OK);
	}

	@Override
	@GetMapping("/search/{searchString}")
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("searchString") String employeeName) {
		// TODO employeeName validation is required
		if (employeeName == null || employeeName.isEmpty()) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
		}
		List<Employee> employeeList = employeeService.getEmployeeByName(employeeName);
		if (employeeList == null) {
			//TODO controllerAdvice
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(employeeList, HttpStatus.OK);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
		// TODO id validation is required
		Employee employee = employeeService.getEmployeeById(id);
		if (employee != null) {
			return new ResponseEntity<>(employee, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@Override
	@GetMapping("/highestSalary")
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		List<Employee> employeeList = employeeService.getEmployees();
		if (employeeList != null && !employeeList.isEmpty()) {
			Optional<Integer> highestSalary = getIntegerStream(employeeList).collect(Collectors.toList()).stream().findFirst();

			if (highestSalary.isPresent()) {
				return new ResponseEntity<>(highestSalary.get(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	private static Stream<Integer> getIntegerStream(List<Employee> employeeList) {
		return employeeList.stream().sorted((e1, e2) -> Integer.compare(e2.getEmployeeSalary(), e1.getEmployeeSalary())).map(Employee::getEmployeeSalary);
	}

	@Override
	@GetMapping("/topTenHighestEarningEmployeeNames")
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		List<Employee> employeeList = employeeService.getEmployees();
		if (employeeList != null && !employeeList.isEmpty()) {
			List<String> topTen = employeeList.stream().sorted((e1, e2) -> Integer.compare(e2.getEmployeeSalary(), e1.getEmployeeSalary())).limit(10).map(Employee::getEmployeeName).collect(Collectors.toList());
			return new ResponseEntity<>(topTen, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@Override
	@PostMapping()
	public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {
		// TODO payload validation is required
		Employee employee = employeeService.createEmployee(employeeInput);
		if (employee != null) {
			return new ResponseEntity<>(employee, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
		// TODO id validation is required
		String employee = employeeService.deleteEmployeeById(id);
		if (employee != null) {
			return new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}


}
