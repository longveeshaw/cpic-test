package org.laotse.github.spring.jpa.repositories;

import java.util.List;

import org.laotse.github.spring.jpa.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

	/**
	 * ���ݷ�������������������SQL
	 * 
	 * @param lastName
	 * @return
	 */
	List<User> findByLastName(String lastName);

}
