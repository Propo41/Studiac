package com.valhalla.studiac.models;

import java.util.HashMap;

public class Result {
    private Double gpa;
    private HashMap<String, String> gradesObtained;   // contains *CourseCode, Grade* for each courses
    private Integer coursesTaken;
    private Double totalCredits;

    public Result() {

    }

    public Result(String type) {
        if (type.equals("init")) {
            this.gpa = 0.0;
            gradesObtained = new HashMap<>();
            coursesTaken = 0;
            totalCredits = 0.0;
        }
    }


    public Result(Double gpa, HashMap<String, String> gradesObtained, Integer coursesTaken, Double totalCredits) {
        this.gpa = gpa;
        this.gradesObtained = gradesObtained;
        this.coursesTaken = coursesTaken;
        this.totalCredits = totalCredits;
    }


    public String getGradesObtainedValue(String key) {
        return gradesObtained.get(key);
    }


    public HashMap<String, String> getGradesObtained() {
        return gradesObtained;
    }


    public void setGradesObtainedValue(String key, String Value) {
        this.gradesObtained.put(key, Value);
    }


    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }


    public void setGradesObtained(HashMap<String, String> courseGrades) {
        this.gradesObtained = courseGrades;
    }


    public Integer getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(Integer coursesTaken) {
        this.coursesTaken = coursesTaken;
    }

    public void setTotalCredits(Double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Double getTotalCredits() {
        return totalCredits;
    }
}
