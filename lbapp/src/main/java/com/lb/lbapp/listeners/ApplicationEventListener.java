package com.lb.lbapp.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.lb.lbapp.service.HealthCheckService;
import com.lb.lbapp.service.LoadBalancerService;

@Component
public class ApplicationEventListener {
	
	@Autowired
	HealthCheckService healthCheckService;

	
	@EventListener
	public void handleContextRefreshEvent(ContextRefreshedEvent contextRefreshedEvent) {
		healthCheckService.doHealthChecks();
	}
	
}
