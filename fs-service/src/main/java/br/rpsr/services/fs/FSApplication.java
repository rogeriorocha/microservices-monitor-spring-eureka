package br.rpsr.services.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import br.rpsr.services.fs.storage.StorageProperties;
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
@EnableConfigurationProperties(StorageProperties.class)
public class FSApplication {

	public static void main(String[] args) {
		SpringApplication.run(FSApplication.class, args);
	}

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.basePackage("br.rpsr.services.fs.controller"))
					.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("FileServer API").description("FileServer API v1.0").build());
	}
 
	/*
	 * @Bean UserRepository repository() { UserRepository repository = new
	 * UserRepository(); repository.add(new User(1L, "Rogerio Rocha"));
	 * repository.add(new User(2L, "Ze das cove")); repository.add(new User(3L,
	 * "Tio doido")); return repository; }
	 * 
	 */}
