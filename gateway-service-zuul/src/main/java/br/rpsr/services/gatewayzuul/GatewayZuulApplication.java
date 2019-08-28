package br.rpsr.services.gatewayzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
//EnableEurekaClient 	// It acts as a eureka client
@EnableZuulProxy		// Enable Zuul
@EnableDiscoveryClient 
public class GatewayZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayZuulApplication.class, args);
	}
	
	/*
	 * @Bean public AddUserNameFilter addUserNameFilter() { return new
	 * AddUserNameFilter(); }
	 * 
	 * 
	 * @Bean public LogRequestFilter logRequestFilter() { return new
	 * LogRequestFilter(); }
	 * 
	 * @Bean public LogResponseFilter logResponseFiltertFilter() { return new
	 * LogResponseFilter(); }
	 */	
	

}