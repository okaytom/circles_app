package com.example.demo;


public class Events {
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String subject;
    private int priorityLevel;
    private String category;
    private String date;

    public Events(String subject, int priorityLevel, String category, String date) {
        this.subject = subject;
        this.priorityLevel = priorityLevel;
        this.category = category;
        this.date = date;
    }

    public Events() {
    }
}