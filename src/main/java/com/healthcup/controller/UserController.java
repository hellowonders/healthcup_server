package com.healthcup.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.healthcup.api.CustomRepository;
import com.healthcup.api.Repository;
import com.healthcup.domain.User;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final Logger LOGGER = Logger.getLogger(UserController.class);

	@Autowired
	Repository repository;
	@Autowired(required = true)
	CustomRepository customRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return repository.findAll();
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "Test Success !";
	}
	
	@RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
	public User findOne(@PathVariable("email") String email) {
		return customRepository.findByEmail(email);
	}
	
	@RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
	public User getUserByAccessToken(@PathVariable("token") String token) {
		return customRepository.findByToken(token);
	}


	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public boolean insertUser(@RequestBody User user) {
		if (null != findOne(user.getEmail()))
			customRepository.updateFBToken(user);
		else
			repository.insert(user);
		return true;
	}

	@RequestMapping(value = "/{email}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable("email") String email) {
		repository.delete(findOne(email));
	}

	@RequestMapping(value = "/bookAppointment", method = RequestMethod.POST)
	public boolean bookAppointment(@RequestBody User user) {
		User usr = findOne(user.getEmail());
		LOGGER.info("Executing bookAppointment, user found "+ usr);
			customRepository.bookAppointment(user);
		return true;
	}
	
	@RequestMapping(value = "/deleteAppointment/{email}", method = RequestMethod.DELETE)
	public boolean deleteAppointment(@PathVariable("email") String email) {
		customRepository.deleteAppointment(email);
		return true;
	}
	
	@RequestMapping(value = "/updateUserType/{email}", method = RequestMethod.PUT)
	public void insertUser(@PathVariable("email") String email) {
		User user = findOne(email);
		if (null != user)
			customRepository.updateUserType(user);
		
	}
}
