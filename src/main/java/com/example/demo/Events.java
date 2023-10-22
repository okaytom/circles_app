package com.example.demo;


public class Events {
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