package br.rpsr.services.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rpsr.services.user.model.User;
import br.rpsr.services.user.repository.UserRepository;

@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	DiscoveryClient discoveryClient;

	@Autowired
	UserRepository repository;

	/*
	 * @GetMapping("/me") public String currentUserName(Authentication
	 * authentication) { return authentication.getName(); }
	 */

	@GetMapping("/teste")
	public String currentUserName() {
		return null;
	}

	@PostMapping("/")
	public User add(@RequestBody User user) {

		LOGGER.info("User add: {}", user);
		return repository.add(user);
	}

	@GetMapping("/")
	public List<User> findAll(@RequestHeader(value = "x-user-header") String userName,
			@RequestHeader MultiValueMap<String, String> headers) {

		System.out.println("**** user TOKEN=" + userName);
		
		
		

		headers.forEach((String key, List<String> list) -> {
			System.out.println(key + " " + list.toString());
		});

		LOGGER.info("user find");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public User findById(@PathVariable("id") Long id) {
		LOGGER.info("User find: id={}", id);
		return repository.findById(id);
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	@GetMapping("/all")
	public List<String> getInstances() {
		return this.discoveryClient.getServices();
	}

}
