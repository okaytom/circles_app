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
    DatePicker datepicker, datepicker_rmdr;
    @FXML
    TextField starttime, endtime, subject, starttime_rmdr, subject_rmdr;
    @FXML
    ChoiceBox<String> occur, st_am_or_pm, et_am_or_pm, occur_rmdr, st_am_or_pm_rmdr;

    @FXML
    ListView<Events> Events_list;

    @FXML
    ListView<Reminders> Reminders_list;

    @FXML
    ComboBox<String> category, category_rmdr;
    @FXML
    Button addevent_btn, removeevent_btn, addreminder_btn, removereminder_btn;

    @FXML
    Slider priority_rmdr;

    @FXML
    Text month_txt;



    /**
     * arraylist that hold Events
     */
    ArrayList<Events> events = new ArrayList<>();

    /**
     * arraylist that hold Events
     */
    ArrayList<Reminders> reminders = new ArrayList<>();

    /**
     * filepath for saved ecents
     */
    String events_filepath = SaveState.devFolder + "/Events.json";


    /**
     * filepath for saved reminders
     */
    String reminders_filepath = SaveState.devFolder + "/Reminders.json";


    public void  drawCalendar(){
        ZonedDateTime calenderview = date;

        // clears the calendar before a draw
        Node grid = calendargrid.getChildren().get(0);
        calendargrid.getChildren().clear();
        calendargrid.getChildren().add(0,grid);
        calendargrid.getStyleClass().add("background");

        //Sets month text to correct Month
        month_txt.setText(calenderview.getMonth().toString());

        // Makes sure calendar view starts at Monday
        while(calenderview.getDayOfWeek().getValue() != 1){ // 1 == Monday
            calenderview = calenderview.minusDays(1);
        }

        // For every day of the week, starting at Monday
        for(int i = 0;i < 7; i++){
            DayOfWeek weekday = calenderview.getDayOfWeek(); // the day of the week i.e monday, tuesday
            int monthday = calenderview.getDayOfMonth(); // 1-31
            Text day = new  Text(weekday.toString() + " " + monthday); // add day to corresponding calendar column
            calendargrid.add(day, i,0);

            // if there is a reminder
            if(reminders.size()!= 0){
                for (Reminders r: reminders){
                    // create new reminder UI
                    Label subject_lbl = new Label(r.getSubject());
                    VBox reminder_display = new VBox();
                    reminder_display.getChildren().add(subject_lbl);
                    if(r.getCategory().equals("School")){
                        reminder_display.getStyleClass().add("school");
                    }
                    else if (r.getCategory().equals("Work")){
                        reminder_display.getStyleClass().add("work");
                    }
                    else{
                        reminder_display.getStyleClass().add("other");
                    }


                    if(r.getOccur().equals("One-Time")) { // if the event is one time
                        if (r.getDate().toString().substring(0, 10).equals(calenderview.toString().substring(0, 10))) { // if date matches
                            // add reminder to calendar
                            calendargrid.add(reminder_display, i , Integer.parseInt(r.getStarttime().substring(0, r.getStarttime().length() - 2)) + 1);
                        }
                    }
                    else if (r.getOccur().equals("Daily")) { // if the reminder is daily
                        if (calenderview.isAfter(r.getDate())) {
                            calendargrid.add(reminder_display, i , Integer.parseInt(r.getStarttime().substring(0, r.getStarttime().length() - 2)) + 1);
                        }
                    }
                    else if (r.getOccur().equals("Weekly")){ // if the reminder is weekly
                        if(calenderview.isAfter(r.getDate()) && r.getDate().getDayOfWeek().equals(calenderview.getDayOfWeek())){
                            calendargrid.add(reminder_display, i , Integer.parseInt(r.getStarttime().substring(0, r.getStarttime().length() - 2)) + 1);
                        }
                    }
                    else if (r.getOccur().equals("Mon-Wed-Fri")){ // if the reminder is Mon-Wed-Fri
                        if(calenderview.isAfter(r.getDate()) && (i == 0 || i == 2 || i == 4)){
                            calendargrid.add(reminder_display, i , Integer.parseInt(r.getStarttime().substring(0, r.getStarttime().length() - 2)) + 1);
                        }
                    }
                    else if (r.getOccur().equals("Tue-Thur")){ // if the reminder is Tue-Thur
                        if(calenderview.isAfter(r.getDate()) && (i == 1 || i == 3)){
                            calendargrid.add(reminder_display, i , Integer.parseInt(r.getStarttime().substring(0, r.getStarttime().length() - 2)) + 1);
                        }
                    }
                }
            }

            if(events.size() != 0){ // if there are events
                for (Events e: events){
                    if(e.getOccur().equals("One-Time")) { // if the event is one time
                        if (e.getDate().toString().substring(0, 10).equals(calenderview.toString().substring(0, 10))) { // if date matches
                            drawEvent(e, calendargrid, i);
                        }
                    }
                    else if (e.getOccur().equals("Daily")) { // if the event is daily
                        if (calenderview.isAfter(e.getDate())) {
                            drawEvent(e, calendargrid, i);
                        }
                    }
                    else if (e.getOccur().equals("Weekly")){ // if the event is weekly
                        if(calenderview.isAfter(e.getDate()) && e.getDate().getDayOfWeek().equals(calenderview.getDayOfWeek())){
                            drawEvent(e, calendargrid, i);
                        }
                    }
                    else if (e.getOccur().equals("Mon-Wed-Fri")){ // if the reminder is Mon-Wed-Fri
                        if(calenderview.isAfter(e.getDate()) && (i == 0 || i == 2 || i == 4)){
                            drawEvent(e, calendargrid, i);
                        }
                    }
                    else if (e.getOccur().equals("Tue-Thur")){ // if the reminder is Tue-Thur
                        if(calenderview.isAfter(e.getDate()) && (i == 1 || i == 3)){
                            drawEvent(e, calendargrid, i);
                        }
                    }
                }
            }
            calenderview = calenderview.plusDays(1);
        }

    }

    private void drawEvent(Events e, GridPane calendargrid, int DayofWeek){
        double event_len = Integer.parseInt(e.getEndtime()) - Integer.parseInt(e.getStarttime());
        int count = 0;
        VBox event_display = new VBox();

        String style_class;
        if(e.getCategory().equals("School")){
            style_class = "school";
        }
        else if (e.getCategory().equals("Work")){
            style_class = "work";
        }
        else{
            style_class = "other";
        }

        event_display.getStyleClass().add(style_class);

        // For drawing the hours,
        while (event_len >= 100) { // while there is still a full hour to draw
            if (count == 0) { // for the header
                Label subject_lbl = new Label(e.getSubject());
                event_display.getChildren().add(subject_lbl);
                // add event to calendar
                calendargrid.add(event_display, DayofWeek , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
            }
            else {
                event_display = new VBox();
                event_display.getStyleClass().add(style_class);
                calendargrid.add(event_display, DayofWeek, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
            }
            event_len -= 100;
            count++;
        }

        if (event_len != 0) { // if there are any minutes left to draw
            event_display = new VBox();
            event_display.getStyleClass().add(style_class);
            event_display.setPrefHeight(event_len / 60 * 16);
            // draw the remaining minutes

            //making the minutes look pretty
            GridPane.setFillHeight(event_display, false);
            GridPane.setValignment(event_display, VPos.TOP);

            // add minutes to calendar
            calendargrid.add(event_display, DayofWeek , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
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
        RemoveReminderPage.setVisible(false);
        AddReminderPage.setVisible(false);
        CalendarPage.toFront();
        drawCalendar();
    }
    @FXML
    private void AddEvent() {
        CalendarPage.setVisible(false);
        AddEventPage.setVisible(true);
        AddEventPage.toFront();

        // need to clear items everytime in case this page has previously been called this sesison
        occur.getItems().clear();
        occur.getItems().addAll("One-Time", "Daily", "Weekly", "Mon-Wed-Fri", "Tue-Thur");
        occur.setValue("One-Time");  // default value

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
                    category.getValue());
        });
    }

    // Tommy
    private void VerifyEventData(int year, int month_num, int day, String subject,
                                 String occur, String starttime, String endtime,
                                 boolean start_am, boolean end_am, String category){
        Events new_event = new Events();

        try{
            // check to see that all text fields were filled
            if (subject == null || occur == null || starttime == null || endtime == null || category == null){
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

            new_event.setDate(year , month_num , day);
            new_event.setSubject(subject);
            new_event.setOccur(occur);
            new_event.setStarttime(Integer.toString(start));
            new_event.setEndtime(Integer.toString(end));
            new_event.setCategory(category);
            System.out.println("The day is " + day + "and the month is" + month_num);
            System.out.println("Event date is :" + new_event.getDate());
            System.out.println("Event subject is: " + new_event.getSubject());
            System.out.println("Event occurence is: " + new_event.getOccur());
            System.out.println("Event start time is: " + new_event.getStarttime());
            System.out.println("Event end time is: " + new_event.getEndtime());
            System.out.println("Event category is: " + new_event.getCategory());
            events.add(new_event);
            SaveState.Save(events_filepath, events); // saves object

            // Updating calendar
            goBack();
        }
        catch (IOException e){
            AlertBox.display("Empty Textfields", "Fill in all textfields");
        }
        catch (IllegalArgumentException i){
            AlertBox.display("Error in time", "Must be in format 'hour:minutes'");
        }
        catch (AssertionError a){
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
                SaveState.Save(events_filepath, events); // saves object
            });
        }
    }

    @FXML
    private void AddReminder() {
        CalendarPage.setVisible(false);
        AddReminderPage.setVisible(true);
        AddReminderPage.toFront();

        occur_rmdr.getItems().clear();
        occur_rmdr.getItems().addAll("One-Time", "Daily", "Weekly","Mon-Wed-Fri", "Tue-Thur");
        occur_rmdr.setValue("One-Time");  // default value

        st_am_or_pm_rmdr.getItems().clear();
        st_am_or_pm_rmdr.getItems().addAll("AM", "PM");
        st_am_or_pm_rmdr.setValue("AM");


        //set it to combobox so user can put in their own category
        category_rmdr.getItems().clear();
        category_rmdr.getItems().addAll("Work", "School");
        category_rmdr.setEditable(true);
        category_rmdr.setValue("School");

        addreminder_btn.getStyleClass().add("button");
        addreminder_btn.setOnAction(e -> {
            // save AM or PM as an integer where AM = 1 and PM = 0
            boolean start_am = true;

            int chosen_day = datepicker_rmdr.getValue().getDayOfMonth();
            int month = datepicker_rmdr.getValue().getMonthValue();
            int year = datepicker_rmdr.getValue().getYear();
            if(st_am_or_pm_rmdr.getValue().equals("PM")){
                start_am = false;
            }

            priority_rmdr.getValue();
            VerifyReminderData(year, month, chosen_day, subject_rmdr.getText(),
                    occur_rmdr.getValue(), starttime_rmdr.getText(), start_am,
                    category_rmdr.getValue(), priority_rmdr.getValue());
        });
    }

    private void VerifyReminderData(int year, int month_num, int day, String subject,
                                 String occur, String starttime,
                                 boolean start_am, String category, double prio
    ){
        Reminders new_reminder = new Reminders();

        try{
            // check to see that all text fields were filled
            if (subject == null || occur == null || starttime == null || endtime == null || category == null){
                throw new IOException();
            }


            // checking start times
            String regex = "[1-9]:[0-5]\\d|1[0-2]:[0-5]\\d";
            if(!(starttime.matches(regex))){
                throw new IllegalArgumentException();
            }

            // we know that this will not cause an exception because of the regex
            int start = Integer.parseInt(starttime.replaceFirst(":", ""));

            if(!start_am){
                start = start + 1200;
            }
            new_reminder.setDate(year , month_num , day);
            new_reminder.setSubject(subject);
            new_reminder.setOccur(occur);
            new_reminder.setStarttime(Integer.toString(start));
            new_reminder.setCategory(category);
            new_reminder.setPriorityLevel(prio);
            System.out.println("The day is " + day + "and the month is" + month_num);
            System.out.println("reminder date is :" + new_reminder.getDate());
            System.out.println("reminder subject is: " + new_reminder.getSubject());
            System.out.println("reminder occurence is: " + new_reminder.getOccur());
            System.out.println("reminder start time is: " + new_reminder.getStarttime());
            System.out.println("reminder category is: " + new_reminder.getCategory());
            System.out.println("reminder priority is: " + new_reminder.getPriorityLevel());
            reminders.add(new_reminder);
            SaveState.Save(reminders_filepath, reminders); // saves object

            // Updating calendar
            goBack();
        }
        catch (IOException t){
            AlertBox.display("Empty Textfields", "Fill in all textfields");
        }
        catch (IllegalArgumentException i){
            AlertBox.display("Error in time", "Must be in format 'hour:minutes'");
        }
    }


    @FXML
    private void RemoveReminder(){
        CalendarPage.setVisible(false);
        RemoveReminderPage.setVisible(true);
        RemoveReminderPage.toFront();

        if(reminders.size() != 0){ // if there are reminders
            Reminders_list.getItems().clear();

            for(Reminders r : reminders){
                Reminders_list.getItems().add(r); // add reminders to listview

            }
            // can only pick one event at a time
            Reminders_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



            removereminder_btn.getStyleClass().add("button");
            removereminder_btn.setOnAction(e -> {
                reminders.remove(Reminders_list.getSelectionModel().getSelectedItem());
                Reminders_list.getItems().remove(Reminders_list.getSelectionModel().getSelectedItem());
                SaveState.Save(reminders_filepath, reminders); // saves object
            });
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Load in Events
        events = SaveState.Load(events_filepath, Events.class);
        reminders = SaveState.Load(reminders_filepath, Reminders.class);

        goBack(); // this returns to the calendar view
    }
}
