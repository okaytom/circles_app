package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    /**
     *  Object for holding the current date time
     */
    ZonedDateTime date = ZonedDateTime.now();


    @FXML
    GridPane calendargrid;

    @FXML
    private Pane AddEventPage, RemoveEventPage, AddReminderPage, RemoveReminderPage, CalendarPage;

    @FXML
    DatePicker datepicker;
    @FXML
    TextField starttime, endtime, subject;
    @FXML
    ChoiceBox<String> occur, st_am_or_pm, et_am_or_pm;

    @FXML
    ListView<Events> Events_list;

    @FXML
    ComboBox<String> category;
    @FXML
    Button addevent_btn, removeevent_btn;



    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<>();

    @FXML
    Text month_txt;


    public void  drawCalendar(){

        ZonedDateTime calenderview = date;

        // clears the calendar before a draw
        Node grid = calendargrid.getChildren().get(0);
        calendargrid.getChildren().clear();
        calendargrid.getChildren().add(0,grid);

        calendargrid.getStyleClass().add("background");


        //month.setFont(new Font(24));
        month_txt.setText(date.getMonth().toString());


        for(int i = 0;i < 7; i++){
            calenderview = date.plusDays(i);
            DayOfWeek weekday = calenderview.getDayOfWeek(); // the day of the week i.e monday, tuesday
            int monthday = calenderview.getDayOfMonth(); // 1-31
            Text day = new  Text(weekday.toString().toLowerCase() + " " + monthday);
            calendargrid.add(day, i,0);



            if(events.size() != 0){ // if there are events
                for (Events e: events){
                    if(e.getOccur().equals("One-Time")) { // if the event is one time
                        if (e.getDate().toString().substring(0, 10).equals(calenderview.toString().substring(0, 10))) { // if date matches
                            double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
                            int count = 0;
                            VBox event_display = new VBox();
                            event_display.getStyleClass().add("event");

                            while (event_len >= 100) { // while there is still a full hour to draw
                                if (count == 0) { // for the header
                                    Label subject_lbl = new Label(e.getSubject());
                                    event_display.getChildren().add(subject_lbl);

                                    // add event to calendar
                                    calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendargrid.add(event_display, i, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                                }
                                event_len -= 100;
                                count++;
                            }

                            if (event_len != 0) { // if there are any minutes left to draw
                                event_display = new VBox();
                                event_display.getStyleClass().add("event");
                                event_display.setPrefHeight(event_len / 60 * 16);
                                // draw the remaining minutes

                                GridPane.setFillHeight(event_display, false);
                                GridPane.setValignment(event_display, VPos.TOP);

                                calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }
                        }
                    }
                    else if (e.getOccur().equals("Daily")) { // if the event is daily
                        if (calenderview.isAfter(e.getDate())) {
                            double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
                            int count = 0;
                            VBox event_display = new VBox();
                            event_display.getStyleClass().add("event");

                            while (event_len >= 100) { // while there is still a full hour to draw
                                if (count == 0) { // for the header
                                    Label subject_lbl = new Label(e.getSubject());
                                    event_display.getChildren().add(subject_lbl);

                                    // add event to calendar
                                    calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                                }
                                event_len -= 100;
                                count++;
                            }

                            if (event_len != 0) { // if there are any minutes left to draw
                                event_display = new VBox();
                                event_display.getStyleClass().add("event");
                                event_display.setPrefHeight(event_len / 60 * 16);
                                // draw the remaining minutes

                                GridPane.setFillHeight(event_display, false);
                                GridPane.setValignment(event_display, VPos.TOP);

                                calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }
                        }
                    }
                    else if (e.getOccur().equals("Weekly")){ // if the event is weekly
                        if(calenderview.isAfter(e.getDate()) && e.getDate().getDayOfWeek().equals(calenderview.getDayOfWeek())){
                            double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
                            int count = 0;
                            VBox event_display = new VBox();
                            event_display.getStyleClass().add("event");

                            while (event_len >= 100) { // while there is still a full hour to draw
                                if (count == 0) { // for the header
                                    Label subject_lbl = new Label(e.getSubject());
                                    event_display.getChildren().add(subject_lbl);

                                    // add event to calendar
                                    calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
                                } else {
                                    event_display = new VBox();
                                    event_display.getStyleClass().add("event");
                                    calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                                }
                                event_len -= 100;
                                count++;
                            }

                            if (event_len != 0) { // if there are any minutes left to draw
                                event_display = new VBox();
                                event_display.getStyleClass().add("event");
                                event_display.setPrefHeight(event_len / 60 * 16);
                                // draw the remaining minutes

                                GridPane.setFillHeight(event_display, false);
                                GridPane.setValignment(event_display, VPos.TOP);

                                calendargrid.add(event_display, i , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
                            }

                        }

                    }


                }
            }
        }

    }


    @FXML
    private void prevWeek(){
        date = date.minusWeeks(1);
        drawCalendar();
    }

    @FXML
    private void nextWeek(){
        date = date.plusWeeks(1);
        drawCalendar();
    }
    @FXML
    private void goBack(){
        CalendarPage.setVisible(true);
        RemoveEventPage.setVisible(false);
        AddEventPage.setVisible(false);
        CalendarPage.toFront();
        drawCalendar();
    }
    @FXML
    private void AddEvent() {
        CalendarPage.setVisible(false);
        AddEventPage.setVisible(true);
        AddEventPage.toFront();

        occur.getItems().clear();
        occur.getItems().addAll("One-Time", "Daily", "Weekly");
        // default value
        occur.setValue("One-Time");


        st_am_or_pm.getItems().clear();
        st_am_or_pm.getItems().addAll("AM", "PM");
        st_am_or_pm.setValue("AM");

        et_am_or_pm.getItems().clear();
        et_am_or_pm.getItems().addAll("AM", "PM");
        et_am_or_pm.setValue("AM");




        //set it to combobox so user can put in their own category
        category.getItems().clear();
        category.getItems().addAll("Work", "School");
        category.setEditable(true);
        category.setValue("School");


        addevent_btn.getStyleClass().add("button");
        addevent_btn.setOnAction(e -> {
            // save AM or PM as an integer where AM = 1 and PM = 0
            boolean start_am = true;
            boolean end_am = true;

            int chosen_day = datepicker.getValue().getDayOfMonth();
            int month = datepicker.getValue().getMonthValue();
            int year = datepicker.getValue().getYear();
            if(st_am_or_pm.getValue().equals("PM")){
                start_am = false;
            }
            if(et_am_or_pm.getValue().equals("PM")){
                end_am = false;
            }
            VerifyEventData(year, month, chosen_day, subject.getText(),
                    occur.getValue(), starttime.getText(), endtime.getText(), start_am, end_am,
                    category.getValue(), 0);
        });


    }

    // Tommy
    private void VerifyEventData(int year, int month_num, int day, String subject,
                                 String occur, String starttime, String endtime,
                                 boolean start_am, boolean end_am, String category, int prio
    ){
        Events new_event = new Events();


        try{
            // check to see that all text fields were filled
            if (year == 0 || month_num == 0 || day == 0 || subject == null
                    || occur == null || starttime == null || endtime == null || category == null){
                throw new IOException();
            }



            // I could not enable assert for some reason so I had to do it the stupid way
            if(month_num == 9 || month_num == 4 || month_num == 6 || month_num == 11){
                if(day < 1 || day > 30){
                    throw new AssertionError();
                }
            }
            else if(month_num == 2  && year % 4 == 0 ){
                if(day < 1 || day > 29){
                    throw new AssertionError();
                }
            }
            else if(month_num == 2){
                if(day < 1 || day > 28){
                    throw new AssertionError();
                }
            }
            else{
                if(day < 1 || day > 31){
                    throw new AssertionError();
                }
            }

            // checking start and end times
            String regex = "[1-9]:[0-5]\\d|1[0-2]:[0-5]\\d";
            if(!(starttime.matches(regex) && endtime.matches(regex))){
                throw new IllegalArgumentException();
            }

            // we know that this will not cause an exception because of the regex
            int start = Integer.parseInt(starttime.replaceFirst(":", ""));
            int end = Integer.parseInt(endtime.replaceFirst(":", ""));


            if(!start_am){
                start = start + 1200;
            }

            if(!end_am){
                end = end + 1200;
            }

            //check to see start time is before end time
            if (start > end){
                throw new ArithmeticException();
            }

            new_event.setDate(year , month_num , day);
            new_event.setSubject(subject);
            new_event.setOccur(occur);
            new_event.setStarttime(Integer.toString(start));
            new_event.setEndtime(Integer.toString(end));
            new_event.setCategory(category);
            new_event.setPriorityLevel(prio);
            System.out.println("The day is " + day + "and the month is" + month_num);
            System.out.println("Event date is :" + new_event.getDate());
            System.out.println("Event subject is: " + new_event.getSubject());
            System.out.println("Event occurence is: " + new_event.getOccur());
            System.out.println("Event start time is: " + new_event.getStarttime());
            System.out.println("Event end time is: " + new_event.getEndtime());
            System.out.println("Event category is: " + new_event.getCategory());
            events.add(new_event);

            // Updating calendar
            goBack();
        }
        catch (IOException t){
            AlertBox.display("Empty Textfields", "Fill in all textfields");
        }
        catch (NumberFormatException e){
            AlertBox.display("Error in day", "Day must be an int");
        }
        catch (AssertionError a){
            AlertBox.display("Error in day", "Day is outside range");
        }
        catch (IllegalArgumentException i){
            AlertBox.display("Error in time", "Must be in format 'hour:minutes'");
        }
        catch (ArithmeticException x){
            AlertBox.display("Error in time", "Start time must be before endtime");
        }


    }


    // Tommy
    @FXML
    private void RemoveEvent(){
        CalendarPage.setVisible(false);
        RemoveEventPage.setVisible(true);
        RemoveEventPage.toFront();

        if(events.size() != 0){ // if there are events
            Events_list.getItems().clear();

            for(Events e : events){
                Events_list.getItems().add(e); // add events to listview

            }
            // can only pick one event at a time
            Events_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



            removeevent_btn.getStyleClass().add("button");
            removeevent_btn.setOnAction(e -> {
                events.remove(Events_list.getSelectionModel().getSelectedItem());
                Events_list.getItems().remove(Events_list.getSelectionModel().getSelectedItem());
            });


        }


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goBack(); // this returns to the calendar view
        // TODO fix datepicker
    }
}
