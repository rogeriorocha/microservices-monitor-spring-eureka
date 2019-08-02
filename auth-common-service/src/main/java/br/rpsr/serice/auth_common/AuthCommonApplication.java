package br.rpsr.serice.auth_common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient 
public class AuthCommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthCommonApplication.class, args);
	}
}
