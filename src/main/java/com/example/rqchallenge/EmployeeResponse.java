package com.example.rqchallenge;

import com.example.rqchallenge.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


public class EmployeeResponse {
private String status;
private List<Employee> data;
private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Employee> getData() {
		return data;
	}

	public void setData(List<Employee> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
