package com.southern.cmps.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Course {
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Integer getConcentrationCode() {
		return concentrationCode;
	}
	public void setConcentrationCode(Integer concentrationCode) {
		this.concentrationCode = concentrationCode;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
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
	private String courseId;
	private String courseName;
	private Integer concentrationCode;
	private Integer hours;
	private String createdBy;
	private Date dateCreated;
	private String modifiedBy;
	private Date dateModified;
}
