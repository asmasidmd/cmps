package com.southern.cmps.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;


@Data
public class Concentration {

	public Integer getConcentrationCode() {
		return concentrationCode;
	}
	public void setConcentrationCode(Integer concentrationCode) {
		this.concentrationCode = concentrationCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<Integer, Course> getCourses() {
		return courses;
	}
	public void setCourses(Map<Integer, Course> courses) {
		this.courses = courses;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	private Integer concentrationCode;
	private String name;
	private Map<Integer, Course> courses;
	private String createdBy;
	private Date dateCreated;
	private String modifiedBy;
	private Date dateModified;
}
