package com.example.demo;

/** TOMMY OJO */

import javafx.application.Platform;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Reminders {
    /**
     * what the reminder is about
     */
    private String subject;

    /**
     * how important the reminder is
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


    /**
     * Timer for scheduling, transcient since it does not have to be saved
     */
    private transient Timer timer;

    /**
     * if the reminder has been scheduled
     */
    private transient boolean scheduled;

    /**
     * Constructor
     */
    public Reminders() {
        timer = new Timer();
        scheduled = false;
    }


    public String getSubject() {
        return subject;
    }

    public double getPriorityLevel() {
        return priorityLevel;
    }

    public String getCategory() {
        return category;
    }

    public String getOccur() {
        return occur;
    }

    public String getStarttime() {
        return starttime;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    private void setDate(int year, int month, int day) {
        this.date = ZonedDateTime.of(year, month,day, 0, 0, 0, 0,ZonedDateTime.now().getZone());
    }

    public String toString(){
        return subject;
    }

    public boolean VerifyReminderData(int year, int month_num, int day, String subject,
                                   String occur, String starttime,
                                   boolean start_am, String category, double prio){
        try{
            // check to see that all text fields were filled
            if (subject.isBlank()  || occur.isBlank() || starttime.isBlank() || category.isBlank() ||
            year == 0 || month_num == 0 || day == 0) {
                throw new IOException();
            }

            // checking start times are in proper format
            String regex = "[1-9]:[0-5]\\d|1[0-2]:[0-5]\\d|0[1-9]:[0-5]\\d";
            if(!(starttime.matches(regex))){
                throw new IllegalArgumentException();
            }

            // we know that this will not cause an exception because of the regex
            int start = Integer.parseInt(starttime.replaceFirst(":", ""));

            if(start >= 1200){
                start = start - 1200; // from 12-1pm, we do not want it to be at 24
            }

            if(!start_am){
                start = start + 1200;
            }

            this.setDate(year , month_num , day);
            this.subject = subject;
            this.occur = occur;
            this.starttime = String.format("%04d", start); // always 4 digits
            this.category = category;
            this.priorityLevel = prio;
            System.out.println("The day is " + day + "and the month is" + month_num);
            System.out.println("Reminder date is :" + this.getDate());
            System.out.println("Reminder subject is: " + this.getSubject());
            System.out.println("Reminder occurence is: " + this.getOccur());
            System.out.println("Reminder start time is: " + this.getStarttime());
            System.out.println("reminder priority is: " + this.getPriorityLevel());
            System.out.println("Reminder category is: " + this.getCategory());

            // schedule the newly validated info, if it is already scheduled, unschedule and make a new Tmer
            if(this.scheduled){
                this.unschedule();
                this.timer = new Timer();
            }
            this.schedule(); // schedules a newly added reminder
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
        String reminder_occur = this.getOccur();

        TimerTask reminder_task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( ()->AlertBox.display("Don't Forget!", reminder_txt));
                // TimerTask() creates a new thread which CANNOT make its own window,
                // the Platform.runLater just allows this timer to create a window
            }
        };

        // I can't schedule the same task twice so I need new tasks for Mon-Wed-Fri and Tue-Thur
        TimerTask reminder_task_2 = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( ()->AlertBox.display("Don't Forget!", reminder_txt));
                // TimerTask() creates a new thread which CANNOT make its own window,
                // the Platform.runLater just allows this timer to create a window
            }
        };

        TimerTask reminder_task_3 = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater( ()->AlertBox.display("Don't Forget!", reminder_txt));
                // TimerTask() creates a new thread which CANNOT make its own window,
                // the Platform.runLater just allows this timer to create a window
            }
        };



        // if the date of the reminder has NOT passed
        Date remind = new Date(date.toEpochSecond()*1000); // takes milliseconds since the epoch on Jan 1 1970
        if(reminder_occur.equals("One-Time")){
            if(ZonedDateTime.now().isBefore(date)){ // if date to be scheduled has not yet pasted
                timer.schedule(reminder_task, remind);
            }
        }
        else if (reminder_occur.equals("Daily")){
            while(ZonedDateTime.now().isAfter(date)){ // move date to after current time
                date = date.plusDays(1);
            }
            timer.scheduleAtFixedRate(reminder_task, new Date(date.toEpochSecond()*1000), 24*3600*1000); // repeats every day
        }
        else if (reminder_occur.equals("Weekly")){
            while(ZonedDateTime.now().isAfter(date)){ // move date to after current time
                date = date.plusDays(7);
            }
            timer.scheduleAtFixedRate(reminder_task, new Date(date.toEpochSecond()*1000), 7*24*3600*1000); // repeats every week

        }
        else if (reminder_occur.equals("Mon-Wed-Fri")){
            while(ZonedDateTime.now().isAfter(date)){ // move date to after current time
                date = date.plusDays(7);
            }

            ZonedDateTime next_mon = date;
            while(next_mon.getDayOfWeek().getValue() != 1 ){ // go to a next monday
                next_mon = next_mon.plusDays(1);
            }

            ZonedDateTime next_wed = date;
            while(next_wed.getDayOfWeek().getValue() != 3 ){ // go to a next wednesday
                next_wed = next_wed.plusDays(1);
            }

            ZonedDateTime next_fri = date;
            while(next_fri.getDayOfWeek().getValue() != 5 ){ // go to a next friday
                next_fri = next_fri.plusDays(1);
            }
            timer.scheduleAtFixedRate(reminder_task, new Date(next_mon.toEpochSecond()*1000), 7*24*3600*1000);
            timer.scheduleAtFixedRate(reminder_task_2, new Date(next_wed.toEpochSecond()*1000), 7*24*3600*1000);
            timer.scheduleAtFixedRate(reminder_task_3, new Date(next_fri.toEpochSecond()*1000), 7*24*3600*1000);
        }
        else if (reminder_occur.equals("Tue-Thur")){
            while(ZonedDateTime.now().isAfter(date)){ // move date to after current time
                date = date.plusDays(7);
            }

            ZonedDateTime next_tue = date;
            while(next_tue.getDayOfWeek().getValue() != 2 ){ // go to a next tuesday
                next_tue = next_tue.plusDays(1);
            }

            ZonedDateTime next_thu = date;
            while(next_thu.getDayOfWeek().getValue() != 4 ){ // go to a next wednesday
                next_thu = next_thu.plusDays(1);
            }

            timer.scheduleAtFixedRate(reminder_task, new Date(next_tue.toEpochSecond()*1000), 7*24*3600*1000);
            timer.scheduleAtFixedRate(reminder_task_2, new Date(next_thu.toEpochSecond()*1000), 7*24*3600*1000);
        }
        this.scheduled = true;
    }

    public void unschedule(){
        timer.cancel();
    }

}