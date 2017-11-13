package com.healthcup.api;

import org.springframework.transaction.annotation.Transactional;

import com.healthcup.domain.User;

@Transactional
public interface CustomRepository {

	public User findByEmail(String email);

	public void updateFBToken(User user);

	public void bookAppointment(User usr);

	public User findByToken(String token);

	public void updateUserType(User user);

	public void deleteAppointment(String email);

}
