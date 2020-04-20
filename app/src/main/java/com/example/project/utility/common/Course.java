package com.example.project.utility.common;

import com.example.project.utility.todo.Task;

import java.util.ArrayList;

public class Course {

    private String mName;
    private String mCode;
    private Double mCredit;
    private ArrayList<Schedule> mRoutine;
    private Instructor mInstructor;

    public Course(String name, String code, Double credit, ArrayList<Schedule> routine, Instructor instructor, ArrayList<Task> todoTasks) {
        mName = name;
        mCode = code;
        mCredit = credit;
        mRoutine = routine;
        mInstructor = instructor;
    }

    public Course(String name, String code, Double credit) {
        mName = name;
        mCode = code;
        mCredit = credit;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Double getCredit() {
        return mCredit;
    }

    public void setCredit(Double credit) {
        mCredit = credit;
    }

    public ArrayList<Schedule> getRoutine() {
        return mRoutine;
    }

    public void setRoutine(ArrayList<Schedule> routine) {
        mRoutine = routine;
    }

    public Instructor getInstructor() {
        return mInstructor;
    }

    public void setInstructor(Instructor instructor) {
        mInstructor = instructor;
    }

}
