package com.serli.open.data.poitiers.bike.shelters.rest.model;

import java.util.Arrays;

public class Ticketmachine {
	
	public Ticketmachine(double[] location, int objectId, int numberTicketmachine) {
		this.location = location;
		this.objectId = objectId;
		this.numberTicketmachine = numberTicketmachine;
	}
	
	public double[] location;
	public int objectId;
	public int numberTicketmachine;
	
	@Override
	public String toString() {
		return "ticketmachine{" +
				"objectId=" + objectId +
				" ,numberticketmachine=" + numberTicketmachine +
				" ,location=" + Arrays.toString(location) +
				"}";
	}

}
