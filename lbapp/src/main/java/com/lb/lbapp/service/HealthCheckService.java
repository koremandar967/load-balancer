package com.lb.lbapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import org.springframework.stereotype.Service;

import com.lb.lbapp.utils.HealthCheckTimerTask;

@Service
public class HealthCheckService {

	private List<Integer> serverPortNumbers = new ArrayList<>(Arrays.asList(9090,9091,9092,9093,9094));
	private volatile Set<Integer> alivePortNumbers = new HashSet<>();
	
	public void doHealthChecks() {
		
		for(Integer portNumber : serverPortNumbers) {
			
			HealthCheckTimerTask healthCheckTimerTask = new HealthCheckTimerTask(alivePortNumbers, portNumber);

			Timer timer = new Timer("Timer");
			timer.scheduleAtFixedRate(healthCheckTimerTask, 0L, 60000L);
			
		}

	}

	public Set<Integer> getAlivePortNumbers() {
		return alivePortNumbers;
	}
	
}
