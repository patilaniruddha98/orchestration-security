package com.org.security;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.org.security.model.Role;
import com.org.security.model.RolePermission;
import com.org.security.model.UserEntity;
import com.org.security.repository.RoleRepository;



@Component
public class MyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
	
	
//	@Autowired
//	PlanRepository planRepository;	
	
	@Autowired
	PasswordEncoder encoder;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String DDL_AUTO;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		if (DDL_AUTO.equals("update")) {
			return;
		}

		
		
		
		
	/*	
		
		UserEntity userEntity = new UserEntity (
			"Nikhil Narasimhan", 
			"nikhil.narasimhan99@gmail.com", 
			encoder.encode("password"), 
			false,
			null,
			null,
			new Date(),
			null
		);
		
		userService.createUserEntity(userEntity, adminRole);
	}*/
	}
}

