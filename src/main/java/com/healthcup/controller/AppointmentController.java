package com.healthcup.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.healthcup.api.AppointmentRepository;
import com.healthcup.domain.Appointment;
import com.healthcup.domain.User;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired(required=true)
	AppointmentRepository repository;
	
	@Autowired
	MongoTemplate template;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Appointment> getAllAppointments() {
		List<Appointment> appointmentList =  repository.findAll();
		Collections.sort(appointmentList, new Comparator<Appointment>() {

			@Override
			public int compare(Appointment arg0, Appointment arg1) {
				return arg0.getUser().getAppointmentDate().compareTo(arg1.getUser().getAppointmentDate());
			}

		});
		return appointmentList;
	}
	
	@RequestMapping(value = "/{email}", method = RequestMethod.GET)
	public Appointment findOne(@PathVariable("email") String email) {
		return template.findOne(Query.query(Criteria.where("user.email").is(email)), Appointment.class);
	}
	
	public void bookAppointment(User user) {
		Appointment appointment = new Appointment();
		appointment.setUser(user);
		repository.save(appointment);
	}
	
	@RequestMapping(value = "/{email}", method = RequestMethod.DELETE)
	public void deleteAppointment(@PathVariable("email") String email) {
		repository.delete(findOne(email));
	}
}
