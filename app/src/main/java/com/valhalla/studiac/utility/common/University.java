package com.valhalla.studiac.utility.common;

import java.util.ArrayList;
import java.util.HashMap;

public class University {

    private String mName;
    private String mDepartment;
    private int mTotalSemester;
    private int mCurrentSemester;
    private ArrayList<Course> mCourses;
    private ArrayList<Routine> mDailyRoutine;
    private ArrayList<Result> semesterResults;
    private HashMap<String, Double> gradeWeights;


    public University(String name, String department, int totalSemester, int currentSemester, ArrayList<Routine> dailyRoutine, ArrayList<Course> courses) {
        mName = name;
        mDepartment = department;
        mTotalSemester = totalSemester;
        mCurrentSemester = currentSemester;
        mDailyRoutine = dailyRoutine;
        mCourses = courses;
    }

    public ArrayList<Routine> getDailyRoutine() {
        return mDailyRoutine;
    }

    public void setDailyRoutine(ArrayList<Routine> dailyRoutine) {
        mDailyRoutine = dailyRoutine;
    }

    public ArrayList<Result> getSemesterResults() {
        return semesterResults;
    }

    public void setSemesterResults(ArrayList<Result> semesterResults) {
        this.semesterResults = semesterResults;
    }

    public HashMap<String, Double> getGradeWeights() {
        return gradeWeights;
    }

    public void setGradeWeights(HashMap<String, Double> gradeWeights) {
        this.gradeWeights = gradeWeights;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public int getTotalSemester() {
        return mTotalSemester;
    }

    public void setTotalSemester(int totalSemester) {
        mTotalSemester = totalSemester;
    }

    public int getCurrentSemester() {
        return mCurrentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        mCurrentSemester = currentSemester;
    }

    public ArrayList<Course> getCourses() {
        return mCourses;
    }

    public void setCourses(ArrayList<Course> courses) {
        mCourses = courses;
    }
}
