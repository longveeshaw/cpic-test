package org.laotse.github.spring.jpa.repositories;

import java.util.List;

import org.laotse.github.spring.jpa.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * 根据方法名的命名规则生成SQL
	 * 
	 * @param lastName
	 * @return
	 */
	List<User> findByLastName(String lastName);

}
