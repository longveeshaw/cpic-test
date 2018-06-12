package org.laotse.github.spring.jpa.api;

import java.util.List;

import org.laotse.github.spring.jpa.entity.User;
import org.laotse.github.spring.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpa/users")
public class UserAPIs {

	@Autowired 
	private UserRepository repository;
	
	@RequestMapping("/")
	public List<User> test() {
		User dave = new User("Dave", "Matthews");
		dave = repository.save(dave);
		
		User carter = new User("Carter", "Beauford");
		carter = repository.save(carter);
		
		List<User> result = repository.findByLastName("Matthews");
		
		return result;
	}
	
}
