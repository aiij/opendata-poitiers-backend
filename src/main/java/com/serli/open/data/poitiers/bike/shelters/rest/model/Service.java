package com.serli.open.data.poitiers.bike.shelters.rest.model;

import java.util.Arrays;

public class Service {
	
	public Service(String name, String theme, double[] location, int objectId, int postCode, String town, String address) {
		this.name = name;
		this.theme = theme;
		this.location = location;
		this.objectId = objectId;
		this.postCode = postCode;
		this.town = town;
		this.address = address;
	}
	
	public String name;
	public String theme;
	public double[] location;
	public int objectId;
	public int postCode;
	public String town;
	public String address;
	
	@Override
	public String toString() {
		return "Service{" +
			   "name=" + name +
			   " , theme=" + theme +
			   " ,location=" + Arrays.toString(location) +
			   " ,objectId=" + objectId +
			   " ,town=" + town +
			   " ,postCode=" + postCode +
			   " ,address=" +address +
			   "}";
	}

}
