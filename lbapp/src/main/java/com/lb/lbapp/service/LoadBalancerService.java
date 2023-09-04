package com.lb.lbapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lb.lbapp.utils.Constants;


@Service
public class LoadBalancerService {

	@Autowired
	HealthCheckService healthCheckService;

	private volatile int allocatorIndex = 0;

	Logger logger = LoggerFactory.getLogger(LoadBalancerService.class);


	@Async("threadPoolTaskExecutor")
	public CompletableFuture<ResponseEntity<?>> handleAsyncRequest() {


		URL url;
		try {
			
			 System.out.println("Request handled with configured executor - "
				      + Thread.currentThread().getName());

			Set<Integer> alivePortNumbers = healthCheckService.getAlivePortNumbers();

			logger.info("alivePortNumbers " + Arrays.toString(alivePortNumbers.toArray()));

			List<Integer> liveServers = new ArrayList<>(alivePortNumbers);

			Integer serverPortNumber = liveServers.get(allocatorIndex);

			String serverUrl = "http://localhost:" + serverPortNumber + "/test";

			url = new URL(serverUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			if (allocatorIndex >= liveServers.size() - 1) {
				allocatorIndex = 0;
			} else {
				allocatorIndex++;
			}

			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				logger.info(response.toString());

				return CompletableFuture.completedFuture(ResponseEntity.ok(response.toString()));

			} else {

				logger.error("GET request did not work.");
				return CompletableFuture.completedFuture((ResponseEntity<?>) ResponseEntity.internalServerError());
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return CompletableFuture.completedFuture((ResponseEntity<?>) ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT));
	}


	
}
