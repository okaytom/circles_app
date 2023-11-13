package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {

    /*
    TODO 1. FIX START/END Time formats
    TODO 2. Fix Calendar scaling
    TODO 3. FIX Time at the left in Calendar
     */

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

    TimerTask reminder_notif = new TimerTask() {
        @Override
        public void run() {

        }
    };


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

    PopupControl popupControl = new PopupControl();


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
            Text day = new  Text(weekday.toString().substring(0,3) + " " + monthday); // add day to corresponding calendar column
            calendargrid.add(day, i,0);

            // if there is a reminder
            if(reminders.size()!= 0){
                for (Reminders r: reminders){
                    // create new reminder UI
                    Label subject_lbl = new Label(r.getSubject());
                    VBox reminder_display = new VBox();

                    reminder_display.getChildren().add(subject_lbl);

                    reminder_display.setShape(new Circle(5));
                    if(r.getCategory().equals("School")){
                        reminder_display.getStyleClass().add("school");
                    }
                    else if (r.getCategory().equals("Work")){
                        reminder_display.getStyleClass().add("work");
                    }
                    else{
                        reminder_display.getStyleClass().add("other");
                    }
                    // if clicked, display reminder
                    reminder_display.setOnMouseClicked( e -> DisplayReminder(r));

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
        GridPane.setMargin(event_display, new Insets(0, 5, 0, 5)); // top, right, bottom, left

        // if clicked, display info
        event_display.setOnMouseClicked( a -> {
            DisplayEvent(e);
        });

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
                GridPane.setMargin(event_display, new Insets(0, 5, 0, 5)); // top, right, bottom, left
                event_display.setOnMouseClicked( a -> {
                    DisplayEvent(e);
                });

                calendargrid.add(event_display, DayofWeek, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
            }
            event_len -= 100;
            count++;
        }

        if (event_len != 0) { // if there are any minutes left to draw
            event_display = new VBox();
            event_display.getStyleClass().add(style_class);
            GridPane.setMargin(event_display, new Insets(0,5,0,5));
            event_display.setOnMouseClicked( a -> {
                DisplayEvent(e);
            });

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
            Events new_event = new Events();

            boolean valid_info;

            valid_info = new_event.VerifyEventData(datepicker.getValue().getYear(), datepicker.getValue().getMonthValue(), datepicker.getValue().getDayOfMonth(),
                    subject.getText(), occur.getValue(), starttime.getText(), endtime.getText(), st_am_or_pm.getValue().equals("AM")
                    , et_am_or_pm.getValue().equals("AM"), category.getValue());


            if(valid_info){
                events.add(new_event);
                SaveState.Save(events_filepath, events); // saves object
                goBack();
            }
        });
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
    private void DisplayEvent(Events event){
        Stage event_window = new Stage();

        // have to address this window before any other open window
        event_window.initModality(Modality.APPLICATION_MODAL);
        event_window.setTitle("Event info");

        GridPane event_layout = new GridPane();
        event_layout.setPadding(new Insets(10, 10, 10, 10));
        event_layout.setHgap(7);
        event_layout.setVgap(7);
        event_layout.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        event_layout.getStyleClass().add("background");

        // UI values for event info
        // label on the left, choice on the right
        Label subject_lbl = new Label("Event");
        event_layout.add(subject_lbl, 0, 0);
        TextField subject = new TextField(event.getSubject());
        event_layout.add(subject, 1, 0);

        Label date_lbl = new Label("Date");
        event_layout.add(date_lbl, 0, 1);
        DatePicker datepicker = new DatePicker(event.getDate().toLocalDate());
        event_layout.add(datepicker, 1, 1);

        Label occur_lbl = new Label("Occurence");
        event_layout.add(occur_lbl, 0, 2);
        ChoiceBox<String> occur = new ChoiceBox<>();
        event_layout.add(occur, 1, 2);

        Label stime_lbl = new Label("Start Time");
        event_layout.add(stime_lbl, 0, 3);
        TextField starttime = new TextField();
        event_layout.add(starttime, 1, 3);
        ChoiceBox<String> st_am_or_pm = new ChoiceBox<>();
        event_layout.add(st_am_or_pm, 2, 3);

        Label etime_lbl = new Label("End Time");
        event_layout.add(etime_lbl, 0, 4);
        TextField endtime = new TextField();
        event_layout.add(endtime, 1, 4);
        ChoiceBox<String> et_am_or_pm = new ChoiceBox<>();
        event_layout.add(et_am_or_pm, 2, 4);


        Label category_lbl = new Label("Category");
        event_layout.add(category_lbl, 0, 5);
        ComboBox<String> category = new ComboBox<>();
        event_layout.add(category, 1, 5);



        // setting start time
        st_am_or_pm.getItems().addAll("AM", "PM");
        if(Integer.parseInt(event.getStarttime()) > 1200){ //check at 1200
            st_am_or_pm.setValue("PM");
            int start_time_pm = Integer.parseInt(event.getStarttime()) - 1200;
            starttime.setText(Integer.toString(start_time_pm));
        }
        else{
            st_am_or_pm.setValue("AM");
            starttime.setText(event.getStarttime());
        }

        // adds colon
        String colon_text = starttime.getText();
        colon_text = colon_text.replaceFirst(colon_text.substring(0,colon_text.length() -2), colon_text.substring(0,colon_text.length() -2) + ":");
        starttime.setText(colon_text);

        // setting end time
        et_am_or_pm.getItems().addAll("AM", "PM");
        if(Integer.parseInt(event.getEndtime()) > 1200){ //check at 1200
            et_am_or_pm.setValue("PM");
            int endtime_time_pm = Integer.parseInt(event.getEndtime()) - 1200;
            endtime.setText(Integer.toString(endtime_time_pm));
        }
        else{
            et_am_or_pm.setValue("AM");
            endtime.setText(event.getEndtime());
        }

        // adds colon
        colon_text = endtime.getText();
        colon_text = colon_text.replaceFirst(colon_text.substring(0,colon_text.length() -2), colon_text.substring(0,colon_text.length() -2) + ":");
        endtime.setText(colon_text);


        // setting occur;
        occur.getItems().addAll("One-Time", "Daily", "Weekly", "Mon-Wed-Fri", "Tue-Thur");
        occur.setValue(event.getOccur());

        // setting category
        category.getItems().addAll("Work", "School");
        category.setValue(event.getCategory());

        // setting everything to non editable
        subject.setEditable(false);
        starttime.setEditable(false);
        endtime.setEditable(false);
        category.setDisable(true);
        st_am_or_pm.setDisable(true);
        et_am_or_pm.setDisable(true);
        occur.setDisable(true);
        datepicker.setEditable(false);
        datepicker.setDisable(true);

        // Button to close
        Button ok = new Button("Ok");
        ok.setOnAction(e -> {
            boolean valid_info;

            valid_info = event.VerifyEventData(datepicker.getValue().getYear(), datepicker.getValue().getMonthValue(), datepicker.getValue().getDayOfMonth(),
                    subject.getText(), occur.getValue(), starttime.getText(), endtime.getText(), st_am_or_pm.getValue().equals("AM")
                    , et_am_or_pm.getValue().equals("AM"), category.getValue());

            if(valid_info){
                SaveState.Save(events_filepath, events); // saves object
                drawCalendar();
                event_window.close();
            }
        });

        event_layout.add(ok, 2, 6);

        // Button to edit
        Button edit = new Button("Edit");
        edit.setOnAction(e ->{
            subject.setEditable(true);
            starttime.setEditable(true);
            endtime.setEditable(true);
            category.setDisable(false);
            st_am_or_pm.setDisable(false);
            et_am_or_pm.setDisable(false);
            occur.setDisable(false);
            datepicker.setDisable(false);

        });

        event_layout.add(edit, 0, 6);
        event_window.setScene(new Scene(event_layout));
        event_window.show();
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
            Reminders new_reminder = new Reminders();

            boolean valid_info;

            valid_info = new_reminder.VerifyReminderData(datepicker_rmdr.getValue().getYear(), datepicker_rmdr.getValue().getMonthValue(),
                    datepicker_rmdr.getValue().getDayOfMonth(), subject_rmdr.getText(),
                    occur_rmdr.getValue(), starttime_rmdr.getText(), st_am_or_pm_rmdr.getValue().equals("AM"),
                    category_rmdr.getValue(), priority_rmdr.getValue());

            if(valid_info){
                reminders.add(new_reminder);
                new_reminder.schedule();
                SaveState.Save(reminders_filepath, reminders); // saves object
                goBack();
            }
        });
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

    private void DisplayReminder(Reminders reminder){
        Stage reminder_window = new Stage();

        // have to address this window before any other open window
        reminder_window.initModality(Modality.APPLICATION_MODAL);
        reminder_window.setTitle("Reminder info");

        GridPane reminder_layout = new GridPane();
        reminder_layout.setPadding(new Insets(10, 10, 10, 10));
        reminder_layout.setHgap(7);
        reminder_layout.setVgap(7);
        reminder_layout.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        reminder_layout.getStyleClass().add("background");

        // UI values for reminder info
        // label on the left, choice on the right
        Label subject_lbl = new Label("Reminder");
        reminder_layout.add(subject_lbl, 0, 0);
        TextField subject = new TextField(reminder.getSubject());
        reminder_layout.add(subject, 1, 0);

        Label date_lbl = new Label("Date");
        reminder_layout.add(date_lbl, 0, 1);
        DatePicker datepicker = new DatePicker(reminder.getDate().toLocalDate());
        reminder_layout.add(datepicker, 1, 1);

        Label occur_lbl = new Label("Occurence");
        reminder_layout.add(occur_lbl, 0, 2);
        ChoiceBox<String> occur = new ChoiceBox<>();
        reminder_layout.add(occur, 1, 2);


        Label stime_lbl = new Label("Start Time");
        reminder_layout.add(stime_lbl, 0, 3);
        TextField starttime = new TextField();
        reminder_layout.add(starttime, 1, 3);
        ChoiceBox<String> st_am_or_pm = new ChoiceBox<>();
        reminder_layout.add(st_am_or_pm, 2, 3);

        Label prio_lbl = new Label("Priority");
        reminder_layout.add(prio_lbl, 0, 4);
        Slider prio = new Slider();
        reminder_layout.add(prio, 1, 4);

        Label category_lbl = new Label("Category");
        reminder_layout.add(category_lbl, 0, 5);
        ComboBox<String> category = new ComboBox<>();
        reminder_layout.add(category, 1, 5);

        // setting start time
        st_am_or_pm.getItems().addAll("AM", "PM");
        if(Integer.parseInt(reminder.getStarttime()) > 1200){ //check at 1200
            st_am_or_pm.setValue("PM");
            int start_time_pm = Integer.parseInt(reminder.getStarttime()) - 1200;
            starttime.setText(Integer.toString(start_time_pm));
        }
        else{
            st_am_or_pm.setValue("AM");
            starttime.setText(reminder.getStarttime());
        }

        // adds colon
        String colon_text = starttime.getText();
        colon_text = colon_text.replaceFirst(colon_text.substring(0,colon_text.length() -2), colon_text.substring(0,colon_text.length() -2) + ":");
        starttime.setText(colon_text);


        // setting occur;
        occur.getItems().addAll("One-Time", "Daily", "Weekly", "Mon-Wed-Fri", "Tue-Thur");
        occur.setValue(reminder.getOccur());

        // setting category
        category.getItems().addAll("Work", "School");
        category.setValue(reminder.getCategory());


        // setting prio
        prio.setValue(reminder.getPriorityLevel());

        // setting everything to non editable
        subject.setEditable(false);
        starttime.setEditable(false);;
        category.setDisable(true);
        st_am_or_pm.setDisable(true);
        occur.setDisable(true);
        prio.setDisable(true);
        datepicker.setEditable(false);
        datepicker.setDisable(true);

        // Button to close
        Button ok = new Button("Ok");
        ok.setOnAction(e -> {
            boolean valid_info;

            valid_info = reminder.VerifyReminderData(datepicker.getValue().getYear(), datepicker.getValue().getMonthValue(), datepicker.getValue().getDayOfMonth(),
                    subject.getText(), occur.getValue(), starttime.getText(), st_am_or_pm.getValue().equals("AM")
                  , category.getValue(), prio.getValue());

            if(valid_info){
                SaveState.Save(reminders_filepath, reminders); // saves object
                drawCalendar();
                reminder_window.close();
            }
        });

        reminder_layout.add(ok, 2, 6);

        // Button to edit
        Button edit = new Button("Edit");
        edit.setOnAction(e ->{
            subject.setEditable(true);
            starttime.setEditable(true);
            category.setDisable(false);
            st_am_or_pm.setDisable(false);
            occur.setDisable(false);
            prio.setDisable(false);
            datepicker.setDisable(false);

        });

        reminder_layout.add(edit, 0, 6);
        reminder_window.setScene(new Scene(reminder_layout));
        reminder_window.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Load in Events
        events = SaveState.Load(events_filepath, Events.class);
        reminders = SaveState.Load(reminders_filepath, Reminders.class);

        for (Reminders reminder: reminders){
            reminder.schedule();
        }

        goBack(); // this returns to the calendar view
    }
}
