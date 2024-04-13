package com.southern.cmps.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.southern.cmps.domain.Concentration;
import com.southern.cmps.domain.Course;

public class ConcentrationDetailExtractor implements ResultSetExtractor<Map<Integer, Concentration>>{

	@Override
	public Map<Integer, Concentration> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Integer, Concentration> concentrationsMap = new HashMap<>();
		
		while(rs.next()) {
			int concentrationCode = rs.getInt("ConcentrationCode");
			if(!concentrationsMap.containsKey(concentrationCode)) {
				Concentration concentration = new Concentration();
				concentration.setConcentrationCode(concentrationCode);
				concentration.setName(rs.getString("Name"));
				concentration.setCourses(new HashMap<Integer, Course>());
		
				concentrationsMap.put(concentrationCode, concentration);
			}
			setCourses(concentrationsMap, rs);
		}
		return concentrationsMap;
	}
	
	private void setCourses(Map<Integer, Concentration> concentrationsMap, ResultSet rs) throws SQLException {
		int concentrationCode = rs.getInt("ConcentrationCode");
		Course course = new Course();
		course.setCourseId(rs.getString("CourseId"));
		course.setCourseName(rs.getString("CourseName"));
		course.setConcentrationCode(concentrationCode);
		course.setHours(rs.getInt("Hours"));
		concentrationsMap.get(concentrationCode).getCourses().put(Integer.valueOf(rs.getString("CourseId")), course);
	}

}
