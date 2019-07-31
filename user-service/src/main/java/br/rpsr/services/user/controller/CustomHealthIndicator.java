package br.rpsr.services.user.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator{

	@Override
	public Health health() { 
		try(Stream<Path> paths = Files.walk(Paths.get("C:\\temp\\test"))){
			paths.filter(Files::isRegularFile)
			     .forEach(System.out::println);
			return Health.up().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Health.down().withDetail("FileAccess", "N").build();
		}
	}

}