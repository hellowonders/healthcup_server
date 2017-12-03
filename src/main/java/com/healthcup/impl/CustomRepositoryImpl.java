package com.healthcup.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.healthcup.api.CustomRepository;
import com.healthcup.domain.Appointment;
import com.healthcup.domain.User;

@Repository
public class CustomRepositoryImpl implements CustomRepository {
	private static final Logger LOGGER = Logger.getLogger(CustomRepositoryImpl.class);
	
	@Autowired
	MongoTemplate template;
	
	@Override
	public User findByEmail(String email) {
		return template.findOne(Query.query(Criteria.where("email").is(email)), User.class);
	}

	@Override
	public void updateFBToken(User user) {
		template.updateFirst(Query.query(Criteria.where("email").is(user.getEmail())), Update.update("fbToken", user.getFbToken()), User.class);
	}
	
	@Override
	public void bookAppointment(User user) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(user.getEmail()));
		User usr = template.findOne(query, User.class);
		if (null != usr) {
			Update update = new Update();
			update.set("appointmentType", user.getAppointmentType());
			update.set("appointmentDate", user.getAppointmentDate());
			update.set("appointmentTime", user.getAppointmentTime());
			update.set("phone", user.getPhone());
			update.set("city", user.getCity());
			update.set("userProblem", user.getUserProblem());
			template.updateFirst(query, update, User.class);
			usr.setAppointmentType(user.getAppointmentType());
			usr.setAppointmentDate(user.getAppointmentDate());
			usr.setAppointmentTime(user.getAppointmentTime());
			usr.setPhone(user.getPhone());
			usr.setCity(user.getCity());
			usr.setUserProblem(user.getUserProblem());
			template.save(new Appointment(usr), "appointments");
		}	
	}

	@Override
	public User findByToken(String token) {
		return template.findOne(Query.query(Criteria.where("fbToken").is(token)), User.class);
	}

	@Override
	public void updateUserType(User user) {
		if (user.getUserType() == null || !user.getUserType().equals("partner")) {
			user.setUserType("partner");
		} else {
			user.setUserType("consumer");
		}
		template.save(user);
	}

	@Override
	public void deleteAppointment(String email) {
		User user = findByEmail(email);
		user.setAppointmentDate(null);
		user.setAppointmentTime(null);
		user.setAppointmentType(null);
		template.save(user);
		template.remove(user, "appointments");
		
	}
}
