package com.southern.cmps.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.southern.cmps.domain.Course;
import com.southern.cmps.response.CmpsResponse;
import com.southern.cmps.response.CmpsResponse.Status;
import com.southern.cmps.service.CmpsService;
import com.southern.cmps.service.exception.CmpsException;
import com.southern.cmps.service.impl.CmpsServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("student")
public class StudentController {

	@Autowired
	private CmpsService cmpsServiceImpl;
	
	@RequestMapping(value="/getStudentDetail",
    method=RequestMethod.GET,
    produces="application/json")
	public ResponseEntity<CmpsResponse> getStudentDetail(@RequestParam String uNumber, HttpServletRequest request) throws CmpsException {
		return new ResponseEntity<CmpsResponse>(new CmpsResponse(Status.SUCCESS, null, cmpsServiceImpl.getStudentDetail(uNumber)),HttpStatus.OK);
	}

	@GetMapping("/concentrations")
	public ResponseEntity<CmpsResponse> getConcentrations(HttpServletRequest request) throws CmpsException {
		return new ResponseEntity<CmpsResponse>(new CmpsResponse(Status.SUCCESS, null, cmpsServiceImpl.getConcentrations()),HttpStatus.OK);
	}
	
	@GetMapping("/report-download-url")
	public ResponseEntity<Resource> reportDownloadUrl(HttpServletRequest request) throws Exception {
		return getTestReport(CmpsServiceImpl.writeToFile("student-balance-sheet.ftl", new HashMap<String,String>()));
	}
	
	 @PostMapping("/importData")
	    public ResponseEntity<String> importData(@RequestParam("file") MultipartFile file) {
	        try {
	            if (file.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
	            }
	            Path filePath = saveUploadedFile(file);
	            cmpsServiceImpl.importDataFromExcel(filePath.toString());
	            return ResponseEntity.ok("Data imported successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during data import: " + e.getMessage());
	        }
	    }
	
	public ResponseEntity<Resource> getTestReport(Resource labReportResponse) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE));
		headers.setContentLength(labReportResponse.contentLength());
		ContentDisposition contentDisposition = ContentDisposition.builder("inline")
		          .filename("Student_BalanceSheet")
		          .build();
		headers.setContentDisposition(contentDisposition);
		return new ResponseEntity<>(labReportResponse, headers, HttpStatus.OK);
	}
	
	@PostMapping("/addCourse")
	public ResponseEntity<String> addCourse(@RequestBody Course course) {
	    try {
	        String message = cmpsServiceImpl.addCourse(course);
	        return ResponseEntity.ok(message);
	    } catch (Exception e) {
	        // Handle other unexpected errors
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An unexected error occurred");
	    }
	}
	
	 private Path saveUploadedFile(MultipartFile file) throws  java.io.IOException  {
	        if (file.isEmpty()) {
	            throw new IllegalStateException("Cannot upload an empty file");
	        }
	        Path uploadDirectory = Paths.get("uploads");
	        if (!Files.exists(uploadDirectory)) {
	            Files.createDirectories(uploadDirectory);
	        }
	        Path filePath = uploadDirectory.resolve(file.getOriginalFilename());
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	        return filePath;
	    }
	 
	 @PostMapping("/addConcentration")
	    public ResponseEntity<String> addConcentration(@RequestParam String name, @RequestParam String createdBy) {
	    	cmpsServiceImpl.addConcentration(name, createdBy);
	        return ResponseEntity.ok("Concentration added successfully");
 }
	 
	    @GetMapping("/getAllConcentrations")
	    public ResponseEntity<Map<Integer, String>> getAllConcentrations() {
	        Map<Integer, String> concentrations = cmpsServiceImpl.getAllConcentrations();
	        return ResponseEntity.ok(concentrations);
	    }
}
