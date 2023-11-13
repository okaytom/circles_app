package com.example.demo;

/** TOMMY OJO */

import javafx.application.Platform;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


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

    public boolean VerifyReminderData(int year, int month_num, int day, String subject,
                                   String occur, String starttime,
                                   boolean start_am, String category, double prio){
        try{
            // check to see that all text fields were filled
            if (subject.isBlank()  || occur.isBlank() || starttime.isBlank() || category.isBlank()){
                throw new IOException();
            }

            // checking start times are in proper format
            String regex = "[1-9]:[0-5]\\d|1[0-2]:[0-5]\\d|0[1-9]:[0-5]\\d";
            if(!(starttime.matches(regex))){
                throw new IllegalArgumentException();
            }

            // we know that this will not cause an exception because of the regex
            int start = Integer.parseInt(starttime.replaceFirst(":", ""));

            if(!start_am){
                start = start + 1200;
                if(start >= 2400){
                    start = start - 1200; // from 12-1pm, we do not want it to be at 24
                }
            }

            this.setDate(year , month_num , day);
            this.setSubject(subject);
            this.setOccur(occur);
            this.setStarttime(String.format("%04d", start)); // always 4 digits
            this.setCategory(category);
            this.setPriorityLevel(prio);
            System.out.println("The day is " + day + "and the month is" + month_num);
            System.out.println("Event date is :" + this.getDate());
            System.out.println("Event subject is: " + this.getSubject());
            System.out.println("Event occurence is: " + this.getOccur());
            System.out.println("Event start time is: " + this.getStarttime());
            System.out.println("reminder priority is: " + this.getPriorityLevel());
            System.out.println("Event category is: " + this.getCategory());
            return true;
        }
        catch (IOException e){
            AlertBox.display("Empty Textfields", "Fill in all textfields");
            return false;
        }
        catch (IllegalArgumentException i){
            AlertBox.display("Error in time", "Must be in format 'hour:minutes'");
            return false;
        }
    }


    public void schedule(){

        int minute = Integer.parseInt(this.getStarttime());
        int hour = 0;
        while (minute >= 100){ // gets the hours and minutes
            minute -= 100;
            hour++;
        }

        ZonedDateTime date = this.getDate();
        date = date.plusHours(hour);
        date = date.plusMinutes(minute);

        String reminder_txt = this.getSubject();

        // if the date of the reminder has NOT passed
        if(date.toEpochSecond()*1000 > System.currentTimeMillis()){
            Date remind = new Date(date.toEpochSecond()*1000); // takes milliseconds since the epoch on Jan 1 1970
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater( ()->AlertBox.display("Don't Forget!", reminder_txt));
                    // TimerTask() creates a new thread which CANNOT make its own window,
                    // the Platform.runLater just allows this timer to create a window
                }
            }, remind);
        }

    }
}

// Alert