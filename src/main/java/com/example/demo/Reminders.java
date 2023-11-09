package com.example.demo;

/** TOMMY OJO */
import java.time.ZonedDateTime;


public class Reminders {
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(double priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(int year, int month, int day) {

        this.date = ZonedDateTime.of(year, month,day, 0, 0, 0, 0,ZonedDateTime.now().getZone());
    }

    /**
     * what the reminder is about
     */
    private String subject;

    /**
     * how important the reminder is TODO
     */
    private double priorityLevel;

    /**
     * School work or other
     */
    private String category;

    /**
     * What the day is the reminder is on
     */
    private ZonedDateTime date;

    /**
     * Starting time of the reminder
     */
    private String starttime;

    /**
     * How often this reminder should occur
     */
    private String occur;

    public String getOccur() {
        return occur;
    }

    public void setOccur(String occur) {
        this.occur = occur;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }



    public Reminders(String subject, int priorityLevel, String category, ZonedDateTime date, String starttime, String occur) {
        this.subject = subject;
        this.priorityLevel = priorityLevel;
        this.category = category;
        this.date = date;
        this.starttime = starttime;
        this.occur = occur;
    }


    public Reminders() {
    }

    public String toString(){

        return subject;
    }
}

// Alert