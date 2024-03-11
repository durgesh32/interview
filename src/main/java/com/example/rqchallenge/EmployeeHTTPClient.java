package com.example.rqchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class EmployeeHTTPClient {

	private String RESOURCE_SERVER = "https://dummy.restapiexample.com";
	Logger logger = LoggerFactory.getLogger(EmployeeHTTPClient.class);

	HttpClient httpClient;

	HttpRequest.Builder requestBuilder;

	@Autowired
	public EmployeeHTTPClient(HttpClient httpClient) {
		this.httpClient = httpClient;
		this.requestBuilder = HttpRequest.newBuilder();
	}

	public EmployeeHTTPClient(HttpClient httpClient, HttpRequest.Builder requestBuilder) {
		this.httpClient = httpClient;
		this.requestBuilder = requestBuilder;
	}

	public String getEmployees(String path) {
		try {
			HttpRequest request = requestBuilder
				.uri(new URI(RESOURCE_SERVER + path))
				.GET()
				.build();
			logger.debug("API call to fetch all employees request id:");
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
				//retry
				logger.error("Rate limit is breached for API request id:");
			} else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
				logger.error("Unable to authorize API call request id:");
			} else if (response.statusCode() != HttpStatus.OK.value()) {
				logger.error("Error while calling the  API request id:");
			}
			return response.body();
		} catch (URISyntaxException | IOException | InterruptedException e) {
			logger.error("Error in API call request id:", e);
		}
		return null;
	}

	public String createEmployee(String data) {
		try {
			HttpRequest request = requestBuilder
				.uri(new URI(RESOURCE_SERVER + "/create"))
				.POST(HttpRequest.BodyPublishers.ofString(data))
				.build();
			logger.debug("API call to fetch all employees");
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
				//retry
				logger.error("Rate limit is breached for API request id:");
			} else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
				logger.error("Unable to authorize API call request id:");
			} else if (response.statusCode() != HttpStatus.CREATED.value()) {
				logger.error("Error while calling the  API request id:");
			}
			return response.body();
		} catch (URISyntaxException | IOException | InterruptedException e) {
			logger.error("Error in API call", e);
		}
		return null;
	}

	public String deleteEmployeeById(String id) {
		try {
			HttpRequest request = requestBuilder
				.uri(new URI(RESOURCE_SERVER + "/delete/" + id))
				.DELETE()
				.build();
			logger.debug("API call to fetch all employees");
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
				//retry
				logger.error("Rate limit is breached for API request id:");
			} else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
				logger.error("Unable to authorize API call request id:");
			} else if (response.statusCode() != HttpStatus.CREATED.value()) {
				logger.error("Error while calling the  API request id:");
			}
			return response.body();
		} catch (URISyntaxException | IOException | InterruptedException e) {
			logger.error("Error in API call", e);
		}
		return null;
	}
}
