package com.serli.open.data.poitiers.bike.shelters.rest.model;

import java.util.Arrays;

public class Park {
	
	public Park(double[] location, int objectId) {
		this.location = location;
		this.objectId = objectId;
	}
	
	public double[] location;
	public int objectId;
	
	@Override
	public String toString() {
		return "Park{" +
				"location=" + Arrays.toString(location) +
				" ,objectId=" + objectId +
				"}";
	}

}
