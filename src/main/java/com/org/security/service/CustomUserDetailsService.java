package com.org.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.security.dao.OAuthDAOService;
import com.org.security.model.CustomUser;
import com.org.security.model.UserEntity;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	OAuthDAOService oauthDAOService;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = null;

		try {
			userEntity = oauthDAOService.getUserDetails(username);

			if (userEntity != null && userEntity.getId() != null && !"".equalsIgnoreCase(userEntity.getId())) {
				CustomUser customUser = new CustomUser(userEntity);
				return customUser;
			} else {
				throw new UsernameNotFoundException("User " + username + " was not found in the database");
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}

	}

}
