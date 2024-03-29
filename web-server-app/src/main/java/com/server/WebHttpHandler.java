package com.server;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalTime;

import com.server.utils.Constants;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class WebHttpHandler implements HttpHandler {

	private int portNumber = 8080;
	
	public WebHttpHandler(int portNumber) {
		this.portNumber = portNumber;
	}

	public void handle(HttpExchange httpExchange) throws IOException {
		String requestParamValue = null;
		if ("GET".equals(httpExchange.getRequestMethod())) {
			requestParamValue = handleGetRequest(httpExchange);
		} else if ("POST".equals(httpExchange)) {
//			requestParamValue = handlePostRequest(httpExchange);
		}
		

		handleResponse(httpExchange, requestParamValue);

		
	}
	
	private String handleGetRequest(HttpExchange httpExchange) {

		System.out.printf("URI: %s; portNumber:  %s; timestamp: %s%n",httpExchange.getRequestURI(),portNumber,LocalTime.now());
		
		return httpExchange.getRequestURI().toString();
	}

	private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();
		StringBuilder responseBuilder = new StringBuilder();

		String uri = httpExchange.getRequestURI().toString();
		
		if(uri.equals(Constants.TEST_URL)) {
			responseBuilder.append("Hello from the web server running on port " + portNumber);
		} 
		
		String response = responseBuilder.toString();
		httpExchange.sendResponseHeaders(200, response.length());
		outputStream.write(response.getBytes());

		outputStream.flush();
		outputStream.close();
	}


	

}
