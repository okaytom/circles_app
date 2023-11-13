package com.example.demo;

/** TOMMY OJO */

import java.io.IOException;
import java.time.ZonedDateTime;


public class Events {
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
     * School work or other
     */
    private String category;

    /**
     * What the day is the event is on
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

    public Events() {
    }

    public String toString(){
        return subject;
    }

    /***
     *  Verifies the information given and sets the value to
     *  the event if it is all good.
     * @param year year being verified, int
     * @param month_num month being verified, int
     * @param day day being verified, int
     * @param subject name of event being verified, string
     * @param occur rate of event being verified, string
     * @param starttime start time being verified, string
     * @param endtime end time being verified, string
     * @param start_am true if start time is am, false if pm
     * @param end_am true if end time is am, false if pm
     * @param category category of event being verified, string
     * @returns true if the data given is valid, false otherwise
     */
    public boolean VerifyEventData(int year, int month_num, int day, String subject,
                                 String occur, String starttime, String endtime,
                                 boolean start_am, boolean end_am, String category){
        try{
            // check to see that all text fields were filled
            if (subject.isBlank()  || occur.isBlank() || starttime.isBlank() || endtime.isBlank() || category.isBlank()){
                throw new IOException();
            }

            // checking start and end times are in proper format
            String regex = "[1-9]:[0-5]\\d|1[0-2]:[0-5]\\d";
            if(!(starttime.matches(regex) && endtime.matches(regex))){
                throw new IllegalArgumentException();
            }

            // we know that this will not cause an exception because of the regex
            int start = Integer.parseInt(starttime.replaceFirst(":", ""));
            int end = Integer.parseInt(endtime.replaceFirst(":", ""));

            if(start >= 1200){
                start = start - 1200; // from 12-1pm, we do not want it to be at 24
            }
            if(end >= 1200){
                end = end - 1200;
            }

            if(!start_am){
                start = start + 1200;
            }

            if(!end_am){
                end = end + 1200;
            }



            //check to see start time is before end time
            if (start > end){
                throw new AssertionError();
            }

            this.setDate(year , month_num , day);
            this.setSubject(subject);
            this.setOccur(occur);
            this.setStarttime(String.format("%04d", start)); // always 4 digits
            this.setEndtime(String.format("%04d", end));
            this.setCategory(category);
            System.out.println("The day is " + day + "and the month is" + month_num);
            System.out.println("Event date is :" + this.getDate());
            System.out.println("Event subject is: " + this.getSubject());
            System.out.println("Event occurence is: " + this.getOccur());
            System.out.println("Event start time is: " + this.getStarttime());
            System.out.println("Event end time is: " + this.getEndtime());
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
        catch (AssertionError a){
            AlertBox.display("Error in time", "Start time must be before endtime");
            return false;
        }
    }
}

