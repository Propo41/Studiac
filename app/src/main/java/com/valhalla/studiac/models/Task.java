package com.valhalla.studiac.models;


import com.valhalla.studiac.holders.ListItem;

public class Task extends ListItem {

    private String description;
    private String additionalNotes;
    private String taskType; // task tag.
    private String category; // takes values: COURSE, Self Study, Others
    private String schedule; // used only for upcoming
    private String createdDate; // the date when the task is created


    public Task() {

    }

    public Task(String description, String additionalNotes, String category, String schedule, String taskType, String createdDate) {
        this.description = description;
        this.additionalNotes = additionalNotes;
        this.taskType = taskType;
        this.category = category;
        this.schedule = schedule;
        this.createdDate = createdDate;
        super.type = "Task";
    }

    public Task(String description, String additionalNotes, String category, String taskType, String createdDate) {
        this.taskType = taskType;
        this.description = description;
        this.additionalNotes = additionalNotes;
        this.category = category;
        this.createdDate = createdDate;
        super.type = "Task";


    }

    public Task(String description, String additionalNotes, String createdDate) {
        this.description = description;
        this.additionalNotes = additionalNotes;
        this.createdDate = createdDate;
        super.type = "Task";


    }


    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
