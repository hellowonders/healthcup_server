package com.healthcup.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@Id
	private long id;
	private String uname;

	@Indexed(unique = true)
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String fbToken;
	private String appointmentType;
	private Date appointmentDate;
	private String appointmentTime;
	private String userType;
	private String userProblem;
	private Prescription healthPrescription;
	private Prescription skinPrescription;
	private Prescription weightLossPrescription;
	private Prescription dentalPrescription;
	private List<Order> orders;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFbToken() {
		return fbToken;
	}

	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserProblem() {
		return userProblem;
	}

	public void setUserProblem(String userProblem) {
		this.userProblem = userProblem;
	}

	public Prescription getHealthPrescription() {
		return healthPrescription;
	}

	public void setHealthPrescription(Prescription healthPrescription) {
		this.healthPrescription = healthPrescription;
	}

	public Prescription getSkinPrescription() {
		return skinPrescription;
	}

	public void setSkinPrescription(Prescription skinPrescription) {
		this.skinPrescription = skinPrescription;
	}

	public Prescription getWeightLossPrescription() {
		return weightLossPrescription;
	}

	public void setWeightLossPrescription(Prescription weightLossPrescription) {
		this.weightLossPrescription = weightLossPrescription;
	}

	public Prescription getDentalPrescription() {
		return dentalPrescription;
	}

	public void setDentalPrescription(Prescription dentalPrescription) {
		this.dentalPrescription = dentalPrescription;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
