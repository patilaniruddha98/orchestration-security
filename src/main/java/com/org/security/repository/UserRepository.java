package com.org.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	boolean existsByEmailId(String emailId);
	
	boolean existsByMobile(String mobile);

	public User findByEmailId(String emailId); 
	
	public User findByMobile(String mobile);
	
	

}
