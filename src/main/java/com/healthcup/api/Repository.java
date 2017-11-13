package com.healthcup.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.healthcup.domain.User;

@Transactional
public interface Repository extends MongoRepository<User, String>{
	
}
