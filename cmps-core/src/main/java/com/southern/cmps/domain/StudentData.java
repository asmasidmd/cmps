package com.southern.cmps.domain;

import java.util.List;

import lombok.Data;

@Data
public class StudentData {
    private List<StudentHeader> studentHeaders;
    private List<StudentDetail> studentDetails;

    public StudentData(List<StudentHeader> studentHeaders, List<StudentDetail> studentDetails) {
        this.studentHeaders = studentHeaders;
        this.studentDetails = studentDetails;
    }

    public List<StudentHeader> getStudentHeaders() {
        return studentHeaders;
    }

    public List<StudentDetail> getStudentDetails() {
        return studentDetails;
    }
}
