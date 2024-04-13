package com.southern.cmps.dao.impl;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.southern.cmps.dao.CmpsDao;
import com.southern.cmps.dao.helper.ConcentrationDetailExtractor;
import com.southern.cmps.dao.helper.StudentDetailExtractor;
import com.southern.cmps.domain.Concentration;
import com.southern.cmps.domain.Course;
import com.southern.cmps.domain.Student;
import com.southern.cmps.domain.StudentData;
import com.southern.cmps.domain.StudentDetail;
import com.southern.cmps.domain.StudentHeader;

@Repository
public class CmpsDaoImpl implements CmpsDao {

	@Autowired
	private NamedParameterJdbcTemplate cmpsNPJTemplate;

	private static final String UPSERT_STUDENT_DETAIL_SQL = 
		    "INSERT INTO tbl_student_detail " +
		    "(UNumber, CourseId, Grade, Semester, Year, CreatedBy, DateCreated, ModifiedBy, DateModified) " +
		    "VALUES (:uNumber, :courseId, :grade, :semester, :year, :createdBy, :dateCreated, :modifiedBy, :dateModified) " +
		    "ON DUPLICATE KEY UPDATE " +
		    "Grade = VALUES(Grade), " +
		    "Semester = VALUES(Semester), " +
		    "Year = VALUES(Year), " +
		    "CreatedBy = VALUES(CreatedBy), " +
		    "DateCreated = VALUES(DateCreated), " +
		    "ModifiedBy = VALUES(ModifiedBy), " +
		    "DateModified = VALUES(DateModified)";


	private static final String UPSERT_STUDENT_HEADER_SQL = 
		    "INSERT INTO tbl_student_header (UNumber, FirstName, LastName, Gender, EmailId, Advisor, CreatedBy, DateCreated, ModifiedBy, DateModified) " +
		    "VALUES (:UNumber, :FirstName, :LastName, :Gender, :EmailId, :Advisor, :CreatedBy, :DateCreated, :ModifiedBy, :DateModified) " +
		    "ON DUPLICATE KEY UPDATE " +
		    "FirstName = VALUES(FirstName), LastName = VALUES(LastName), Gender = VALUES(Gender), " +

		    "DateCreated = VALUES(DateCreated), ModifiedBy = VALUES(ModifiedBy), DateModified = VALUES(DateModified)";
	
	private static final String GET_CONCENTRATIONS = "SELECT a.ConcentrationCode,a.Name,b.CourseId,b.CourseName,b.Hours FROM cmps.tbl_concentration a inner join cmps.tbl_courses b On a.ConcentrationCode = b.ConcentrationCode";
	private static final String GET_STUDENT_DETAIL = "SELECT a.UNumber,a.FirstName,a.LastName,a.Gender,a.EmailId,b.CourseId,b.Grade,b.Semester,b.Year,c.CourseName,d.SubConcentrationCode AS ConcentrationCode FROM tbl_student_header a INNER JOIN tbl_student_detail b ON a.UNumber = b.UNumber INNER JOIN tbl_courses c ON b.CourseId = c.CourseId INNER JOIN tbl_sub_concentration d ON c.ConcentrationCode = d.SubConcentrationCode WHERE a.UNumber = :uNumber";

	@Override
	public Student getStudentDetail(String uNumber) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("uNumber", uNumber);
		return cmpsNPJTemplate.query(GET_STUDENT_DETAIL, params, new StudentDetailExtractor());
	}

	@Override
	public Map<Integer, Concentration> getConcentrations() {
		return cmpsNPJTemplate.query(GET_CONCENTRATIONS, new ConcentrationDetailExtractor());
	}

	@Override
	@Transactional
	public void batchInsertStudentDetails(StudentData studentData)throws RuntimeException {
		
		   try {
	    // Extract the list of StudentDetail and StudentHeader from StudentData
	    List<StudentDetail> studentDetailList = studentData.getStudentDetails();
	    List<StudentHeader> studentHeaderList = studentData.getStudentHeaders();
	    
	    studentDetailList.forEach(detail -> {
	        if (detail.getModifiedBy() == null) {
	            detail.setModifiedBy(null);
	        }
	        if (detail.getDateModified() == null) {
	            detail.setDateModified(null);
	        }
	    });
	    
	    studentHeaderList.forEach(header -> {
	        if (header.getDateModified() == null) {
	            header.setDateModified(null);
	        }
	        if (header.getModifiedBy() == null) {
	            header.setDateModified(null);
	        }
	        if (header.getGender() == null) {
	            // Set a default value or handle the error
	            header.setGender("A"); // Only if a sensible default exists
	        }
	    });
	    // Convert the lists to arrays of SqlParameterSource for batch operations
	    SqlParameterSource[] detailBatch = SqlParameterSourceUtils.createBatch(studentDetailList.toArray());
	    SqlParameterSource[] headerBatch = SqlParameterSourceUtils.createBatch(studentHeaderList.toArray());

	    // Perform the batch updates for both student details and headers
	    cmpsNPJTemplate.batchUpdate(UPSERT_STUDENT_DETAIL_SQL, detailBatch);
	    cmpsNPJTemplate.batchUpdate(UPSERT_STUDENT_HEADER_SQL, headerBatch);
		   
	   } catch (DataAccessException e) {
	        // Log the exception if needed
	        // Rethrow as a runtime exception to ensure rollback
	        throw new RuntimeException("Failed to batch insert student details", e);
	    }
	   
   }
	public String addCourse(Course course) {
	    String sql = "INSERT INTO tbl_courses (CourseId, CourseName, ConcentrationCode, Hours, CreatedBy, DateCreated) VALUES (:courseId, :courseName, :concentrationCode, :hours, :createdBy, NOW())";

	    SqlParameterSource namedParameters = new MapSqlParameterSource()
	            .addValue("courseId", course.getCourseId())
	            .addValue("courseName", course.getCourseName())
	            .addValue("concentrationCode", course.getConcentrationCode())
	            .addValue("hours", course.getHours())
	            .addValue("createdBy", course.getCreatedBy());

	    try {
	        cmpsNPJTemplate.update(sql, namedParameters);
	        return "Course added successfully";
	    } catch (DataIntegrityViolationException e) {
	        if (e.getMessage().contains("Duplicate entry")) {
	            return "Course ID already exists in the database.";
	        } else {
	            throw e; // rethrow other exceptions
	        }
	    }
	}

	 

	@Override
	public void addConcentration(String name, String createdBy) {
	    String sql = "INSERT INTO tbl_concentration (Name, CreatedBy, DateCreated) VALUES (:name, :createdBy, :dateCreated)";

	    SqlParameterSource namedParameters = new MapSqlParameterSource()
	            .addValue("name", name)
	            .addValue("createdBy", createdBy)
	            .addValue("dateCreated", LocalDateTime.now());

	    cmpsNPJTemplate.update(sql, namedParameters);
	}
	
	 public Map<Integer, String> findAllConcentrations() {
	        String sql = "SELECT ConcentrationCode, Name FROM tbl_concentration";
	        return cmpsNPJTemplate.query(sql, (ResultSet rs) -> {
	            Map<Integer, String> concentrations = new HashMap<>();
	            while (rs.next()) {
	                concentrations.put(rs.getInt("ConcentrationCode"), rs.getString("Name"));
	            }
	            return concentrations;
	        });
	    }

}




