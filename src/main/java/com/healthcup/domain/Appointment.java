package com.healthcup.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "appointments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment {
	
	public Appointment() {}
	
	public Appointment(User user) {
		this.user = user;
	}
	
	@Id
	private long id;
	private boolean completed;
	private User user; 
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
