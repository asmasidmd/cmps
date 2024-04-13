package com.southern.cmps.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.IOException;
import com.itextpdf.text.DocumentException;
import com.southern.cmps.dao.CmpsDao;
import com.southern.cmps.domain.Concentration;
import com.southern.cmps.domain.Course;
import com.southern.cmps.domain.Student;
import com.southern.cmps.domain.StudentBalanceSheetInfo;
import com.southern.cmps.domain.StudentData;
import com.southern.cmps.domain.StudentDetail;
import com.southern.cmps.domain.StudentHeader;
import com.southern.cmps.service.CmpsService;
import com.southern.cmps.service.exception.CmpsException;
import com.southern.cmps.util.UtilValidate;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Service
public class CmpsServiceImpl implements CmpsService{
	
	private static final Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);

	  static {
	    configuration.setDefaultEncoding("UTF-8");
	    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	    configuration.setIncompatibleImprovements(Configuration.VERSION_2_3_22);

	  }
	
	@Autowired
	private CmpsDao cmpsDaoImpl;
	
	@Override
	public Student getStudentDetail(String uNumber) throws CmpsException {
		if(UtilValidate.isEmpty(uNumber)) {
			throw new CmpsException("Invalid UNumber");
		}
		Student student = cmpsDaoImpl.getStudentDetail(uNumber);
		if(UtilValidate.isEmpty(student)) {
			throw new CmpsException("Invalid Student Information");
		}
		return student;
	}
	
	@Override
	public Map<Integer, Concentration> getConcentrations() throws CmpsException {
		Map<Integer, Concentration> concentrations = cmpsDaoImpl.getConcentrations();
		if (UtilValidate.isEmpty(concentrations)) {
			throw new CmpsException("Invalid Concentrations");
		}
		return concentrations;
	}
	
	@Override
	public StudentBalanceSheetInfo getStudentBalanceSheetInfo(String uNumber) throws CmpsException {
		Student student = getStudentDetail(uNumber);
		Map<Integer, Concentration> concentrations = getConcentrations();
		StudentBalanceSheetInfo info = new StudentBalanceSheetInfo();
//		info.setFirstName(student.getFirstName());
//		info.setuNumber(student.getUNumber());
//		info.setConcentrationsInfo(concentrations);
		return info;
	}
	

	
	public static Resource writeToFile(String templateName, Map<String, String> map) throws Exception {		
			FileTemplateLoader templateLoader = new FileTemplateLoader(new File("/Users/dr.sudhir_trivedi/eclipse-workspace/cmps/cmpsWeb/src/main/resources/templates"));
		    configuration.setTemplateLoader(templateLoader);
	    map.put("studentInfo","info");
		StringWriter strWriter = new StringWriter();  
		Template template = configuration.getTemplate(templateName);
	    template.process(map, strWriter);

	    
		File file2 = new File("/Users/dr.sudhir_trivedi/Downloads/TOC_U01996353.pdf");
	    HtmlConverter.convertToPdf(strWriter.toString(), new FileOutputStream(file2));

		return new ByteArrayResource(Files.readAllBytes(file2.toPath()));
    }
	
	public static void main(String args[]) throws Exception {
		System.out.println(writeToFile("student-balance-sheet.ftl", new HashMap<String,String>()).toString());
	}

	@Override
	@Transactional
	public void importDataFromExcel(String filePath) {
	
        StudentData studentDetails = readStudentsFromExcel(filePath);
        cmpsDaoImpl.batchInsertStudentDetails(studentDetails);
		
	}


	
	private Map<String, Integer> getColumnMap(Row headerRow) {
	    Map<String, Integer> columnMap = new HashMap<>();
	    for (Cell cell : headerRow) {
	        if (cell != null && cell.getCellType() == CellType.STRING) {
	            String headerValue = cell.getStringCellValue().trim(); // Trim the cell value
	            columnMap.put(headerValue, cell.getColumnIndex());
	        }
	    }
	    return columnMap;
	}

	    private String getStringValueFromCell(Row row, int cellIndex) {
	        Cell cell = row.getCell(cellIndex);
	        return cell != null ? cell.getStringCellValue().trim() : "";
	    }
	    
	    
	    public StudentData readStudentsFromExcel(String excelFilePath) {
	        List<StudentHeader> studentsHeaderList = new ArrayList<>();
	        List<StudentDetail> studentDetailList = new ArrayList<>();

	        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
	             Workbook workbook = WorkbookFactory.create(fis)) {

	            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	                Sheet sheet = workbook.getSheetAt(i);
	                Map<String, Integer> columnMap = getColumnMap(sheet.getRow(0)); // Header row

	                for (Row row : sheet) {
	                    if (row.getRowNum() == 0) continue; // Skip the header row

	                    // Extract data for StudentHeader
	                    StudentHeader studentHeader = extractStudentHeader(row, columnMap);
	                    studentsHeaderList.add(studentHeader);

	                    // Extract data for StudentDetail
	                    StudentDetail studentDetail = extractStudentDetail(row, columnMap);
	                    studentDetailList.add(studentDetail);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Handle exceptions
	        }

	        return new StudentData(studentsHeaderList, studentDetailList);
	    }
	    

	    
	    private String determineSemesterBasedOnYear(String yearCode) {
	        if (yearCode == null || yearCode.isEmpty()) {
	            return ""; // or handle error
	        }
	        
	        char lastChar = yearCode.charAt(yearCode.length() - 1);
	        switch (lastChar) {
	            case 'F':
	                return "FALL";
	            case 'S':
	                return "SPRING";
	            default:
	                return ""; // or handle error or default case
	        }
	    }
	    
	    private StudentHeader extractStudentHeader(Row row, Map<String, Integer> columnMap) {
	        StudentHeader studentHeader = new StudentHeader();
	        // Set properties for studentHeader using columnMap and row
	        // Use appropriate keys from columnMap to extract data for studentHeader
	        // Example: studentHeader.setFirstName((String) getStringValueFromCell(row, columnMap.get("FirstName")));
	        // Handle different header names based on the sheet structure
            studentHeader.setuNumber((String) getStringValueFromCell(row,columnMap.get("Banner ID")));
            studentHeader.setFirstName((String) getStringValueFromCell(row,columnMap.get("Name"))); // Assuming there's a FirstName column
            studentHeader.setLastName((String) getStringValueFromCell(row,columnMap.get("Name")));
            studentHeader.setCreatedBy("admin");
            studentHeader.setDateCreated(LocalDateTime.now());
	        return studentHeader;
	    }

	    private StudentDetail extractStudentDetail(Row row, Map<String, Integer> columnMap) {
	     
	        // Set properties for studentDetail using columnMap and row
	        // Use appropriate keys from columnMap to extract data for studentDetail
	        // Handle different header names based on the sheet structure
	        
            StudentDetail studentDetail = new StudentDetail();
            studentDetail.setuNumber((String) getStringValueFromCell(row,columnMap.get("Banner ID")));
        
            // Check for different column names for Course/CourseId
            String courseId = columnMap.containsKey("Course") ? getStringValueFromCell(row, columnMap.get("Course")) 
                                                              : getStringValueFromCell(row, columnMap.get("Course Id"));
            studentDetail.setCourseId(removeHyphenAndFollowingCharacters(courseId));

            // Check for different column names for FinalGrade/Grade
            String grade = columnMap.containsKey("Final Grade") ? getStringValueFromCell(row, columnMap.get("Final Grade")) 
                                                               : getStringValueFromCell(row, columnMap.get("Grade"));
            studentDetail.setGrade(grade);

            // Check for different column names for Sem/Term
            String semester = columnMap.containsKey("Sem") ? getStringValueFromCell(row, columnMap.get("Sem")) 
                                                           : getStringValueFromCell(row, columnMap.get("Term"));
                 
            studentDetail.setSemester(determineSemesterBasedOnYear(semester));
            studentDetail.setYear(extractYear(semester));
            studentDetail.setCreatedBy("admin");
            studentDetail.setDateCreated(LocalDateTime.now());
	        return studentDetail;
	    }
	    
	    private String extractYear(String yearCode) {
	        if (yearCode == null || yearCode.length() < 2) {
	            throw new IllegalArgumentException("Invalid year code");
	        }
	        // Remove the last character and return the substring
	        return yearCode.substring(0, yearCode.length() - 1);
	    }
	    
	    private String removeHyphenAndFollowingCharacters(String input) {
	        if (input == null) {
	            return null; // or you could throw an IllegalArgumentException
	        }
	        int hyphenIndex = input.indexOf('-');
	        if (hyphenIndex != -1) {
	            return input.substring(0, hyphenIndex);
	        }
	        return input; // No hyphen found, return the original input
	    }
	    
	   
	    public String addCourse(Course course) {
	    	return cmpsDaoImpl.addCourse(course);
	    
	    }
	    
	    public void addConcentration(String name, String createdBy) {
	    	cmpsDaoImpl.addConcentration(name, createdBy);
	    }

		@Override
		public Map<Integer, String> getAllConcentrations() {
			return cmpsDaoImpl.findAllConcentrations();
		}


}
