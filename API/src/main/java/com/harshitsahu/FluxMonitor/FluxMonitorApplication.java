package com.harshitsahu.FluxMonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FluxMonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(FluxMonitorApplication.class, args);
	}
}