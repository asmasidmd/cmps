package com.southern.cmps.dao;

import java.util.List;
import java.util.Map;

import com.southern.cmps.domain.Concentration;
import com.southern.cmps.domain.Course;
import com.southern.cmps.domain.Student;
import com.southern.cmps.domain.StudentData;
import com.southern.cmps.domain.StudentDetail;

public interface CmpsDao {
	
	public Map<Integer, Concentration> getConcentrations();

	Student getStudentDetail(String uNumber);

	public void batchInsertStudentDetails(StudentData studentDetails);
	
	public String addCourse(Course course);

	public void addConcentration(String name, String createdBy);
	
	public Map<Integer, String> findAllConcentrations();
	
	
}
