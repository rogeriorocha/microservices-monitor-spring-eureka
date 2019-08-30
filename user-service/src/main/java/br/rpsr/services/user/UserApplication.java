package br.rpsr.services.user;

import java.util.ArrayList;
import java.util.List;

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
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
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
		/*
		 * ParameterBuilder parameterBuilder = new ParameterBuilder(); parameterBuilder
		 * .name("Authorization") //.description("") .modelRef(new ModelRef("string"))
		 * .parameterType("header") .required(false)
		 * .defaultValue("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvbWFyIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTU2NzE3NDg2OSwiZXhwIjoxNTY4NjQ2MDk4fQ._t_Gf_igPim0LQ9TTY4aeBdsieiYgrj5W-qRMJ7pmivmzcsRc3JLxDjppY8dWS4cIZ0ZTBr9oigU_4P3Xvq5yw"
		 * ) .build();
		 * 
		 * 
		 * List<Parameter> aParameters = new ArrayList<>();
		 * aParameters.add(parameterBuilder.build());
		 */
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.rpsr.services.user.controller"))
				.paths(PathSelectors.any()).build()
				//.globalOperationParameters(aParameters)
				.apiInfo(
						new ApiInfoBuilder().version("1.0").title("User API").description("User API v1.0").build()
				);
	}

	@Bean
	UserRepository repository() {
		UserRepository repository = new UserRepository();
		repository.add(new User(1L, "Rogerio Rocha"));
		repository.add(new User(2L, "Tio doido"));
		repository.add(new User(3L, "Texugo"));
		repository.add(new User(4L, "Arau mennnn"));
		return repository;
	}

	@Configuration
	class RestTemplateConfig {
		@Bean
		@LoadBalanced // Load balance between service instances running at different ports.
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

}
