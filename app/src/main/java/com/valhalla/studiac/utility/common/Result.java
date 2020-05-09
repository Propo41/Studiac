package com.valhalla.studiac.utility.common;

import java.util.HashMap;

public class Result {
    private Double gpa;
    private HashMap<String, String> courseGrades;
    private Integer coursesTaken;

    public Result(Double gpa, HashMap<String, String> courseGrades, Integer coursesTaken) {
        this.gpa = gpa;
        this.courseGrades = courseGrades;
        this.coursesTaken = coursesTaken;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public HashMap<String, String> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(HashMap<String, String> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public Integer getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(Integer coursesTaken) {
        this.coursesTaken = coursesTaken;
    }
}
