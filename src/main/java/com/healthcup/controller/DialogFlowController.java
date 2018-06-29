package com.healthcup.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1Context;
import com.google.api.services.dialogflow.v2beta1.model.GoogleCloudDialogflowV2beta1WebhookResponse;
import com.healthcup.domain.Appointment;
import com.healthcup.domain.User;

@RestController
@RequestMapping("/dialogflow")
public class DialogFlowController {

	@Autowired(required = true)
	AppointmentController appointmentController;
	@Autowired(required = true)
	UserController userController;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public GoogleCloudDialogflowV2beta1WebhookResponse process(@RequestBody String json) {
		JSONObject reqBody,queryRes = null,intent;
		List<GoogleCloudDialogflowV2beta1Context> outputContexts = null;
		GoogleCloudDialogflowV2beta1WebhookResponse response = new GoogleCloudDialogflowV2beta1WebhookResponse();
	
		System.out.println("SERVER IN: " + json.toString());
		String email = "", uname = "", fulfilmentText = "Unable to process the request.";
		try {
			ObjectMapper mapper = new ObjectMapper();
			reqBody = new JSONObject(json);
			queryRes = reqBody.getJSONObject("queryResult");
			intent = queryRes.getJSONObject("intent");
			
			if (queryRes.getString("queryText").equalsIgnoreCase("exit")) {
//				return "{\"fulfillmentText\": \"Good Bye !\"}";
			}
			JSONObject params = queryRes.getJSONObject("parameters");
			outputContexts = mapper.readValue(queryRes.getString("outputContexts"), TypeFactory.defaultInstance().constructCollectionType(List.class,  
					GoogleCloudDialogflowV2beta1Context.class));
			if (params.has("email")) {
				email = params.getString("email");
			} 
			if (params.has("uname") ) {
				uname = params.get("uname").toString().split(" ")[0];
			}
			if (intent.getString("displayName").equalsIgnoreCase("Default Welcome Intent")) {
				Appointment appointment = appointmentController.findOne(email);
				if (appointment == null)
					fulfilmentText = "Hi " + uname
							+ ", Welcome to HealthCup, the one stop solution for all health problems. Would you like to book an appointment ?";
				else {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					fulfilmentText = "Hi " + uname + ", you have an existing appointment scheduled on "+formatter.format(appointment.getUser().getAppointmentDate())+" at "+ appointment.getUser().getAppointmentTime()+" Would you like to reschedule/ cancel it ?";
				}
			} else if (intent.getString("displayName").equalsIgnoreCase("ScheduleAppointment") || intent.getString("displayName").equalsIgnoreCase("Welcome Intent - Yes")) {
				if (email.isEmpty()) {throw new Exception();}
				fulfilmentText = appointBook(params);
				contextClear(outputContexts);
				return response.setFulfillmentText(fulfilmentText);
			}  else if (intent.getString("displayName").equalsIgnoreCase("CancelAppointment")) {
				if (email.isEmpty()) {throw new Exception();}
				appointmentController.deleteAppointment(email);
				fulfilmentText = "Appointment cancelled !";
				contextClear(outputContexts);
				return response.setFulfillmentText(fulfilmentText);
			} else if (intent.getString("displayName").equalsIgnoreCase("RescheduleAppointment - yes")) {
				if (email.isEmpty()) {throw new Exception();}
				appointmentController.deleteAppointment(email);
				fulfilmentText = appointBook(params);
				contextClear(outputContexts);
				return response.setFulfillmentText(fulfilmentText);
			}
			response.setFulfillmentText(fulfilmentText);
			response.setOutputContexts(outputContexts);
			System.out.println("SERVER OUT: " + response.toString());
		} catch (Exception e) {
			e.printStackTrace();
			contextClear(outputContexts);
			response.setFulfillmentText("Some error occured.");
		}
		
		return response;

	}

	private void contextClear(List<GoogleCloudDialogflowV2beta1Context> outputContexts) {
		for (int i=0; i < outputContexts.size(); i++) {
			GoogleCloudDialogflowV2beta1Context context = outputContexts.get(i);
			context.setLifespanCount(0);
		}
	}

	private String appointBook(JSONObject params) throws Exception {
		String fulfilmentText = "Unable to Book Appointment", bookDateStr = null, startTime = null, endTime = null;
		bookDateStr = (String) params.getString("date");
		String slot = (String) params.getString("slot");
//		JSONArray slotList =  (JSONArray) params.get("slot");
//		startTime = (String) slotList.getJSONObject(0).getString("startTime");
//		endTime = (String) slotList.getJSONObject(0).getString("endTime");
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");
		DateFormat timeFormatter = new SimpleDateFormat("h a");
		DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		
		Date appointmentDate = formatter.parse(bookDateStr);
//		Date appointmentStartTime = formatter.parse(startTime);
//		Date appointmentEndTime = formatter.parse(endTime);
		User usr = userController.findOne(params.getString("email"));
		usr.setAppointmentDate(appointmentDate);
//		String timeRange = timeFormatter.format(appointmentStartTime) +"-"+ timeFormatter.format(appointmentEndTime);
		usr.setAppointmentTime(slot);
		appointmentController.bookAppointment(usr);
		fulfilmentText = "Appointment booked for "+ dateFormatter.format(appointmentDate)+" at "+slot;
		return fulfilmentText;
	}

}
