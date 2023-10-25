package com.example.demo;


import javafx.collections.ArrayChangeListener;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(int year, int month, int day) {

        this.date = ZonedDateTime.of(year, month,day, 0, 0, 0, 0,ZonedDateTime.now().getZone());
    }

    /**
     * what the event is about
     */
    private String subject;

    /**
     * how important the event is TODO
     */
    private int priorityLevel;

    /**
     * School work or other
     */
    private String category;

    /**
     * What the day is event is on
     */
    private ZonedDateTime date;

    /**
     * Starting time of the event
     */
    private String starttime;

    /**
     * Ending time of the event
     */
    private String endtime;

    /**
     * How often this event should occur
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

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Events(String subject, int priorityLevel, String category, ZonedDateTime date, String starttime, String endtime, String occur) {
        this.subject = subject;
        this.priorityLevel = priorityLevel;
        this.category = category;
        this.date = date;
        this.starttime = starttime;
        this.endtime = endtime;
        this.occur = occur;
    }


    public Events() {
    }
}