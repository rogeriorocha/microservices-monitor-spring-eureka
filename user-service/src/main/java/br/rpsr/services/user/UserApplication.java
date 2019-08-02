package br.rpsr.services.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import br.rpsr.services.user.model.User;
import br.rpsr.services.user.repository.UserRepository;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
@EnableCircuitBreaker // Enable circuit breakers
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.rpsr.services.user.controller"))
				.paths(PathSelectors.any()).build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("User API").description("User API v1.0").build());
	}

	@Bean
	UserRepository repository() {
		UserRepository repository = new UserRepository();
		repository.add(new User(1L, "Rogerio Rocha"));
		repository.add(new User(2L, "Ze das cove"));
		repository.add(new User(3L, "Tio doido"));
		return repository;
	}

	@Configuration
	class RestTemplateConfig {

		// Create a bean for restTemplate to call services
		@Bean
		@LoadBalanced // Load balance between service instances running at different ports.
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}
