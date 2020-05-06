package com.valhalla.studiac.utility.todo;


public class Task {

    private String mDescription;
    private String mAdditionalNotes;
    private String mType; // takes string "current-tasks/ current-week/ upcoming"
    private String mCategory;
    private String mSchedule;

    public Task(String description, String additionalNotes, String category, String schedule, String type) {
        mDescription = description;
        mAdditionalNotes = additionalNotes;
        mType = type;
        mCategory = category;
        mSchedule = schedule;
    }

    public Task(String description, String additionalNotes,  String category, String type) {
        mType = type;
        mDescription = description;
        mAdditionalNotes = additionalNotes;
        mCategory = category;

    }

    public Task(String description, String additionalNotes) {
        mDescription = description;
        mAdditionalNotes = additionalNotes;
        mType = "N/A";
        mCategory = "Quick Task";
        mSchedule = "N/A";

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

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getSchedule() {
        return mSchedule;
    }

    public void setSchedule(String schedule) {
        mSchedule = schedule;
    }
}
