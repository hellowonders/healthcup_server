package com.healthcup.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.POST;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcup.domain.Appointment;
import com.healthcup.domain.User;

@RestController
@RequestMapping("/dialogflow")
public class DialogFlowController {

	@Autowired(required = true)
	AppointmentController appointmentController;
	@Autowired(required = true)
	UserController userController;

	@RequestMapping("/")
	@POST
	public String process(@RequestBody String body) {
		System.out.println("SERVER : " + body);
		String email = "", uname = "", fulfilmentText = "Unable to process the request.";

		try {
			JSONObject reqBody = new JSONObject(body);
			JSONObject queryRes = reqBody.getJSONObject("queryResult");
			JSONObject intent = queryRes.getJSONObject("intent");
			
			if (queryRes.getString("queryText").equalsIgnoreCase("exit") || queryRes.getString("queryText").equalsIgnoreCase("cancel")) {
				return "{\"fulfillmentText\": \"Good Bye !\"}";
			}
			JSONObject params = queryRes.getJSONObject("parameters");

			if (params.has("email")) {
				email = params.getString("email");
			} else if (queryRes.has("outputContexts")) {
				JSONArray jsonArray = queryRes.getJSONArray("outputContexts");
				JSONObject jsonArrayObj = jsonArray.getJSONObject(0);
				JSONObject outContextparams = jsonArrayObj.getJSONObject("parameters");
				email = outContextparams.getString("email");
				
			}
			if (intent.getString("displayName").equalsIgnoreCase("Default Welcome Intent")) {
				JSONArray jsonArray = queryRes.getJSONArray("outputContexts");
				JSONObject jsonArrayObj = jsonArray.getJSONObject(0);
				JSONObject outContextparams = jsonArrayObj.getJSONObject("parameters");
				
				uname =  outContextparams.getString("uname").split(" ")[0];
				email = outContextparams.getString("email");
				Appointment appointment = appointmentController.findOne(email);
				if (appointment == null)
					fulfilmentText = "Hi " + uname
							+ ", Welcome to HealthCup, the one stop solution for all health problems. Would you like to book an appointment ?";
				else {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					fulfilmentText = "Hi " + uname + ", you have an existing appointment scheduled on "+formatter.format(appointment.getUser().getAppointmentDate())+" at "+ appointment.getUser().getAppointmentTime()+" Would you like to reschedule/ cancel it ?";
				}
			} else if (intent.getString("displayName").equalsIgnoreCase("ScheduleAppointment")) {
				fulfilmentText = appointBook(email, params);
			}  else if (intent.getString("displayName").equalsIgnoreCase("CancelAppointment")) {
				appointmentController.deleteAppointment(email);
				fulfilmentText = "Appointment cancelled !";
			} else if (intent.getString("displayName").equalsIgnoreCase("RescheduleAppointment - FollowUp")) {
				appointmentController.deleteAppointment(email);
				fulfilmentText = appointBook(email, params);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"fulfillmentText\": \"Oops! Some error occured.\"}";
		}

		return "{\"fulfillmentText\": \"" + fulfilmentText + "\"}";

	}

	private String appointBook(String email, JSONObject params) throws JSONException, ParseException {
		String fulfilmentText;
		JSONArray jsonArray = params.getJSONArray("slot");
		JSONObject slot = jsonArray.getJSONObject(0);
		String bookDateStr = params.getString("date");
		String startTime = slot.getString("startTime");
		String endTime = slot.getString("endTime");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");
		DateFormat timeFormatter = new SimpleDateFormat("h a");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		
		Date appointmentDate = formatter.parse(bookDateStr);
		Date appointmentStartTime = formatter.parse(startTime);
		Date appointmentEndTime = formatter.parse(endTime);
		User usr = userController.findOne(email.split(":")[1]);
		usr.setAppointmentDate(appointmentDate);
		String timeRange = timeFormatter.format(appointmentStartTime) +"-"+ timeFormatter.format(appointmentEndTime);
		usr.setAppointmentTime(timeRange);
		appointmentController.bookAppointment(usr);
		fulfilmentText = "Appointment booked for "+ dateFormatter.format(appointmentDate)+" between "+timeRange;
		return fulfilmentText;
	}

}
