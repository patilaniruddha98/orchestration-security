package com.org.security.dao;

import com.org.security.model.UserEntity;

public interface OAuthDAOService {

	public UserEntity getUserDetails(String emailId);
}
