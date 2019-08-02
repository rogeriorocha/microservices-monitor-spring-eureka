package br.service.auth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
//EnableFeignClients
@EnableSwagger2

public class ApplicationAuth {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationAuth.class, args);
	}

}
