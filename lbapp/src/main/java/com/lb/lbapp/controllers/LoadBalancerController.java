package com.lb.lbapp.controllers;

import static java.lang.Thread.currentThread;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lb.lbapp.config.SpringAsyncConfig;
import com.lb.lbapp.service.LoadBalancerService;

@RestController
public class LoadBalancerController {
	
	@Autowired
	LoadBalancerService loadBalancerService;

	private volatile int hitCounter = 0;
	
	Logger logger = LoggerFactory.getLogger(LoadBalancerController.class);
	
	@GetMapping("/")
	public CompletableFuture<ResponseEntity<?>> handleRequests() {
		
		System.out.printf("Thread : %s; request hit counter : %s%n",currentThread().getName(), ++hitCounter);
		
		return loadBalancerService.handleAsyncRequest();
		
	}
	
}
