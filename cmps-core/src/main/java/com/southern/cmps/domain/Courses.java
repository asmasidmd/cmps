package com.southern.cmps.domain;

import java.util.List;

import lombok.Data;

@Data
public class Courses {
	
	private String courseName;
	private List<CourseDetail> courses;
	
}
