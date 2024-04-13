package com.southern.cmps.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.southern.cmps.domain.CourseDetail;
import com.southern.cmps.domain.Student;
import com.southern.cmps.util.UtilValidate;

public class StudentDetailExtractor implements ResultSetExtractor<Student> {

	@Override
	public Student extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, CourseDetail> courseMap = new HashMap<>();
		Map<Integer, CourseDetail> conCourseMap = new HashMap<>();
		Student student = new Student();
		while(rs.next()) {
			String uNumber = rs.getString("UNumber");
			if(UtilValidate.isEmpty(student.getUNumber())) {
		//		student.setUNumber(uNumber);
				student.setFirstName(rs.getString("FirstName"));
				student.setLastName(rs.getString("LastName"));
				student.setGender(rs.getString("Gender"));
				student.setEmailId(rs.getString("EmailId"));		
			}
			if(!courseMap.containsKey(rs.getString("CourseId"))) {
				courseMap.put(rs.getString("CourseId"), new CourseDetail());
			}
			if(!conCourseMap.containsKey(rs.getInt("ConcentrationCode"))) {
				conCourseMap.put(rs.getInt("ConcentrationCode"), new CourseDetail());
			}
			setCourseDetail(courseMap.get(rs.getString("CourseId")), rs);
			setCourseDetail(conCourseMap.get(rs.getInt("ConcentrationCode")), rs);				

		}
	    student.setCourses(courseMap.values().stream().collect(Collectors.toList()));
	    student.setCoursesMap(conCourseMap);
	    return student;
	}
	
	private void setCourseDetail(CourseDetail courseDetail, ResultSet rs) throws SQLException {
		courseDetail.setCourseId(rs.getString("CourseId"));
		courseDetail.setCourseName(rs.getString("CourseName"));
		courseDetail.setGrade(rs.getString("Grade"));
		courseDetail.setSemester(rs.getString("Semester"));
		courseDetail.setYear(rs.getDate("Year"));
		courseDetail.setConcentrationCode(rs.getInt("ConcentrationCode"));
	}

}
