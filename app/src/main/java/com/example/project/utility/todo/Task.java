package com.example.project.utility.todo;

public class Task {
    private String description;
    private String additionalNotes;
    private String schedule;
    private String category;
    private String type;


    public Task(String description, String additionalNotes, String schedule, String category, String type) {
        this.description = description;
        this.additionalNotes = additionalNotes;
        this.schedule = schedule;
        this.category = category;
        this.type = type;
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
