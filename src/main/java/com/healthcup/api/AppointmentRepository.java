package com.healthcup.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.healthcup.domain.Appointment;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String>{
	
}
