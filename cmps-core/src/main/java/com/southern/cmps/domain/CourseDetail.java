package com.southern.cmps.domain;

import java.util.Date;

import lombok.Data;

@Data
public class CourseDetail {

	private String courseId;
	private String courseName;
	private Integer concentrationCode;
	private String csNo;
	private String hours;
	private String grade;
	private String semester;
	private Date year;
}
