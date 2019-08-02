package br.rpsr.services.user.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator{
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomHealthIndicator.class);

	@Override
	public Health health() { 
		try(Stream<Path> paths = Files.walk(Paths.get("C:\\temp\\test"))){
			paths.filter(Files::isRegularFile)
			     .forEach(System.out::println);
			
			System.out.println(new Date());
			LOGGER.debug("UP");
			
			return Health.up().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.debug("DOWN");
			return Health.down().withDetail("FileAccess", "N").build();
		}
	}

}