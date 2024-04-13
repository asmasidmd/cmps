package com.southern.cmps.domain;

import java.util.Map;

import lombok.Data;

@Data
public class StudentBalanceSheetInfo {

	public String getuNumber() {
		return uNumber;
	}
	public void setuNumber(String uNumber) {
		this.uNumber = uNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Map<Integer, Concentration> getConcentrationsInfo() {
		return concentrationsInfo;
	}
	public void setConcentrationsInfo(Map<Integer, Concentration> concentrationsInfo) {
		this.concentrationsInfo = concentrationsInfo;
	}
	private String uNumber;
	private String firstName;
	private String lastName;
	private Map<Integer, Concentration> concentrationsInfo;

	
}
