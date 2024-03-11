package com.example.rqchallenge.employees;

import com.example.rqchallenge.EmployeeHTTPClient;
import com.example.rqchallenge.EmployeeResponse;
import com.example.rqchallenge.entity.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

	private final EmployeeHTTPClient httpClient;

	private final ObjectMapper objectMapper;

	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	public EmployeeService(EmployeeHTTPClient httpClient, ObjectMapper objectMapper) {
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
	}

	public List<Employee> getEmployees() {
		String body = httpClient.getEmployees("/api/v1/employees");
		EmployeeResponse employeeResponse;
		try {
			if (body == null) {
				return new ArrayList<>();
			} else {
				// TODO readValue method has vulnerability issues
				employeeResponse = objectMapper.reader().forType(EmployeeResponse.class).readValue(body);
				logger.info("Get call to fetch all employees is successfully request id:");
			}
		} catch (JsonProcessingException e) {
			logger.error("Unable to parse the response, fetch all employee request id:", e);
			return new ArrayList<>();
		}
		return employeeResponse.getData();
	}

	public List<Employee> getEmployeeByName(String employeeName) {
		String body = httpClient.getEmployees("/api/v1/employees");
		EmployeeResponse employeeResponse;
		try {
			if (body == null) {
				return new ArrayList<>();
			} else {
				employeeResponse = objectMapper.reader().forType(EmployeeResponse.class).readValue(body);
				logger.info("Get call to fetch all employees is successfully request id:");
			}
		} catch (JsonProcessingException e) {
			logger.error("Unable to parse the response, fetch all employee request id:", e);
			return new ArrayList<>();
		}
		// employeeName null check required
		return employeeResponse.getData().stream().filter(e -> employeeName.equals(e.getEmployeeName())).collect(Collectors.toList());
	}

	public Employee getEmployeeById(String id) {
		String body = httpClient.getEmployees("/api/v1/employee/" + id);
		try {
			if (body == null) {
				return null;
			} else {
				Map json = objectMapper.readValue(body, new TypeReference<>() {
				});
				logger.info("Get call to fetch all employees is successfully");
				if (json.containsKey("data")) {
					return objectMapper.convertValue(json.get("data"), Employee.class);
				}
			}
		} catch (JsonProcessingException e) {
			logger.error("Unable to parse the response, fetch all employee", e);
		}
		return null;
	}

	public Employee createEmployee(Map<String, Object> employeeInput) {
		try {
			String body = httpClient.createEmployee(objectMapper.writeValueAsString(employeeInput));
			if (body == null) {
				return null;
			} else {
				Map json = objectMapper.readValue(body, new TypeReference<>() {
				});
				logger.info("Get call to fetch all employees is successfully");
				if (json.containsKey("data")) {
					return objectMapper.convertValue(json.get("data"), Employee.class);
				}
			}
		} catch (JsonProcessingException e) {
			logger.error("Unable to parse the response, fetch all employee", e);
		}
		return null;

	}

	public String deleteEmployeeById(String id) {
		try {
			String body = httpClient.deleteEmployeeById(id);
			if (body == null) {
				return null;
			} else {
				Map json = objectMapper.readValue(body, new TypeReference<>() {
				});
				logger.info("Get call to fetch all employees is successfully");
				if (json.containsKey("status") && json.get("status").equals("success")) {
					return "success";
					// TODO employee name as per requirement
				}
			}
		} catch (JsonProcessingException e) {
			logger.error("Unable to parse the response, fetch all employee", e);
		}
		return null;
	}

}
