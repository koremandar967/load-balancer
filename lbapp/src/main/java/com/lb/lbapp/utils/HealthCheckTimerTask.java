package com.lb.lbapp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HealthCheckTimerTask extends TimerTask {

	private int portNumber = 0;
	private Set<Integer> alivePortNumbers = new HashSet<>();
	
	Logger logger = LoggerFactory.getLogger(HealthCheckTimerTask.class);
	
	public HealthCheckTimerTask() {
		
	}
	
	public HealthCheckTimerTask(Set<Integer> alivePortNumbers, int portNumber) {
		this.portNumber = portNumber;
		this.alivePortNumbers = alivePortNumbers;
//		System.out.printf("Timer thread scheduled for port number: %s at %s%n",portNumber,LocalDateTime.now());
	}
	
	@Override
	public void run() {
		
		if(portNumber != 0) {
			
//			System.out.printf("Running for port number : %s at %s%n",portNumber,LocalDateTime.now());
			
			String serverUrl = "http://localhost:" + portNumber + "/health";
			
			URL url;
			try {
				url = new URL(serverUrl);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				
				int responseCode = con.getResponseCode();
				
				if (responseCode == HttpURLConnection.HTTP_OK) { 
					
					alivePortNumbers.add(portNumber);
					
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();
					
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
					System.out.println(response.toString());
					
				} else {
					logger.error("GET request did not work.");
					alivePortNumbers.remove(portNumber);
				}
				
			} catch (Exception e) {
				alivePortNumbers.remove(portNumber);
				System.out.printf("Server is unreachable at: %s%n",portNumber);
			}

			
		}
		
	}

}
