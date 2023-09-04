package com.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.server.utils.Constants;
import com.sun.net.httpserver.HttpServer;

public class WebServer {

	public static void main(String[] args) {

		try {

			int [] portNumbers = new int[] {9090,9092,9093,9094};

			List<HttpServer> serverList = new ArrayList<>();
			
			for(int portNumber : portNumbers) {

				HttpServer server = HttpServer.create(new InetSocketAddress("localhost", portNumber), 0);
				
				ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
				
				WebHttpHandler webHttpHandler = new WebHttpHandler(portNumber);
				
				server.createContext(Constants.TEST_URL,webHttpHandler);
				server.createContext(Constants.HEALTH_URL,webHttpHandler);
				server.setExecutor(threadPoolExecutor);
				server.start();
				
				serverList.add(server);
				
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
