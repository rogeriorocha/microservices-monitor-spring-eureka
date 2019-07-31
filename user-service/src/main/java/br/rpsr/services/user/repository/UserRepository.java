package br.rpsr.services.user.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.rpsr.services.user.model.User;

public class UserRepository {

	private List<User> users = new ArrayList<>();

	public User add(User user) {
		user.setId((long) (users.size() + 1));
		users.add(user);
		return user;
	}

	public User findById(Long id) {
		Optional<User> user = users.stream().filter(a -> a.getId().equals(id)).findFirst();
		if (user.isPresent())
			return user.get();
		else
			return null;
	}

	public List<User> findAll() {
		return users;
	}
}