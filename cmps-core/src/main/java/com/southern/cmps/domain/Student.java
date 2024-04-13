package com.southern.cmps.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Student {

	public String getuNumber() {
		return uNumber;
	}
	public void setuNumber(String uNumber) {
		this.uNumber = uNumber;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<CourseDetail> getCourses() {
		return courses;
	}
	public void setCourses(List<CourseDetail> courses) {
		this.courses = courses;
	}
	public Map<Integer, CourseDetail> getCoursesMap() {
		return coursesMap;
	}
	public void setCoursesMap(Map<Integer, CourseDetail> coursesMap) {
		this.coursesMap = coursesMap;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getAdvisor() {
		return advisor;
	}
	public void setAdvisor(String advisor) {
		this.advisor = advisor;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	private String uNumber;
	private String firstName;
	private String lastName;
	private List<CourseDetail> courses;
	private Map<Integer,CourseDetail> coursesMap;
	private String gender;
	private String emailId;
	private String advisor;
	private String createdBy;
	private LocalDateTime dateCreated;
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getUNumber() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
