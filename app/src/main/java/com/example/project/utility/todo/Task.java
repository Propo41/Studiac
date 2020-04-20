package com.example.project.utility.todo;


import androidx.core.util.Pair;

public class Task {

    private String mDescription;
    private String mAdditionalNotes;
    private String mType;
    // takes string "current-tasks/ current-week/ upcoming"
    // takes object "Course,  null, null"
    private Pair<String, Object> mCategory;
    private String mSchedule;

    public Task(String description, String additionalNotes, Pair<String, Object> category, String schedule, String type) {
        mDescription = description;
        mAdditionalNotes = additionalNotes;
        mType = type;
        mCategory = category;
        mSchedule = schedule;
    }

    public Task(String description, String additionalNotes, Pair<String, Object> category, String type) {
        mType = type;
        mDescription = description;
        mAdditionalNotes = additionalNotes;
        mCategory = category;

    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getAdditionalNotes() {
        return mAdditionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        mAdditionalNotes = additionalNotes;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Pair<String, Object> getCategory() {
        return mCategory;
    }

    public void setCategory(Pair<String, Object> category) {
        mCategory = category;
    }

    public String getSchedule() {
        return mSchedule;
    }

    public void setSchedule(String schedule) {
        mSchedule = schedule;
    }
}
