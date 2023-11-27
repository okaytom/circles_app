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
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalendarController implements Initializable, Searchable {

    /*
    TODO 1. Fix Calendar scaling
     */

    /**
     *  Object for holding the current date time
     */
    private ZonedDateTime date = ZonedDateTime.now();

    /**
     * arraylist that hold Events
     */
    public static ArrayList<Events> events = new ArrayList<>();

    /**
     * arraylist that hold reminders
     */
    public static ArrayList<Reminders> reminders = new ArrayList<>();

    /**
     * filepath for saved ecents
     */
    private static String events_filepath = SaveState.devFolder + "/Events.json";


    /**
     * filepath for saved reminders
     */
    private static String reminders_filepath = SaveState.devFolder + "/Reminders.json";


    /**
     * FXML view objects
     */
    @FXML
    private GridPane calendargrid;
    @FXML
    private Pane AddEventPage, RemoveEventPage, AddReminderPage, RemoveReminderPage, CalendarPage;
    @FXML
    private DatePicker datepicker, datepicker_rmdr;
    @FXML
    private TextField starttime, endtime, subject, starttime_rmdr, subject_rmdr;
    @FXML
    private ChoiceBox<String> occur, st_am_or_pm, et_am_or_pm, occur_rmdr, st_am_or_pm_rmdr;
    @FXML
    private ListView<Events> Events_list;
    @FXML
    private ListView<Reminders> Reminders_list;
    @FXML
    private ComboBox<String> category, category_rmdr;
    @FXML
    private Button addevent_btn, removeevent_btn, addreminder_btn, removereminder_btn;
    @FXML
    private Slider priority_rmdr;
    @FXML
    private Label month_txt;


    /**
     * draws the calendargrid
     */
    private void  drawCalendar(){
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
            Label day = new Label(weekday.toString().substring(0,3) + " " + monthday); // add day to corresponding calendar column
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

    /**
     *
     * @param e event that is being drawn
     * @param calendargrid grid that the event is being drawn on
     * @param DayofWeek starting at 0 for Monday
     */
    private void drawEvent(Events e, GridPane calendargrid, int DayofWeek){
        int endtime = Integer.parseInt(e.getEndtime());
        int starttime = Integer.parseInt(e.getStarttime());
        double endtime_minutes = endtime % 100; // the minutes in end time e.g for 3:20pm, this value will be 20
        double starttime_minutes = starttime % 100; // the minutes in start time
        // the difference in hours to represent how many event_display_bodys have to be drawn. -1 because the header is outside the loop
        double event_len = (endtime/100 - starttime/100) -1;
        int count = 1; // represents grid spaces AFTER the header that must be drawn i.e. header
        VBox event_display_body;

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

        VBox event_display_header = new VBox(); // header is the one with the label for the subject
        GridPane.setMargin(event_display_header, new Insets(0, 5, 0, 5)); // top, right, bottom, left
        event_display_header.setOnMouseClicked( a -> {// if clicked, display info
            DisplayEvent(e);
        });
        event_display_header.getStyleClass().add(style_class);
        Label subject_lbl = new Label(e.getSubject());

        if(event_len == -1){ // special case where starttime and endtime in the same hour i.e. 2:15 and 2:45
            double dif = endtime_minutes - starttime_minutes;
            event_display_header.setPrefHeight((dif / 60) * 16); // set height according to how many minutes need to be drawn
            event_display_header.setMaxHeight((dif / 60) * 16);

            //editting the position based on when the minutes start/end
            GridPane.setFillHeight(event_display_header, false);
            if(starttime_minutes >= 0 && starttime_minutes <= 10){
                GridPane.setValignment(event_display_header, VPos.TOP);
            }
            else if (endtime_minutes <= 59 && endtime_minutes >= 50 ){
                GridPane.setValignment(event_display_header, VPos.BOTTOM);
            }
            else{
                GridPane.setValignment(event_display_header, VPos.CENTER);
            }
            event_display_header.setLayoutX(10);
            subject_lbl.setFont(new Font((dif/60)*12)); // adjusting the font according to the size of the event header

            event_display_header.getChildren().add(subject_lbl);

            // add event_display at column DayofWeek and row
            calendargrid.add(event_display_header, DayofWeek , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
            return;
        }


        if(starttime_minutes != 0){ // draw the header
            event_display_header.setPrefHeight((1-starttime_minutes / 60) * 16); // set height according to how many minutes need to be drawn
            event_display_header.setMaxHeight((1-starttime_minutes / 60) * 16);
            // draw the remaining minutes

            //making the minutes look pretty
            GridPane.setFillHeight(event_display_header, false);
            GridPane.setValignment(event_display_header, VPos.BOTTOM);

            subject_lbl.setFont(new Font((1-starttime_minutes/60)*12)); // adjusting the font according to the size of the event header
            event_display_header.getChildren().add(subject_lbl);


            calendargrid.add(event_display_header, DayofWeek , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
        }
        else{
            event_display_header.getChildren().add(subject_lbl);
            calendargrid.add(event_display_header, DayofWeek , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1);
        }


        // For drawing the hours,
        while (event_len != 0) { // while there is still a full hour to draw
            // for GridPane, each node has to be a new object, which is why I have to repeat this everytime
            event_display_body = new VBox();
            event_display_body.getStyleClass().add(style_class);
            GridPane.setMargin(event_display_body, new Insets(0, 5, 0, 5)); // top, right, bottom, left
            event_display_body.setOnMouseClicked( a -> {
                DisplayEvent(e);
            });

            calendargrid.add(event_display_body, DayofWeek, Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);

            event_len -= 1;
            count++;
        }

        if (endtime_minutes != 0) { // if there are any minutes left to draw
            VBox event_display_tail = new VBox();
            event_display_tail.getStyleClass().add(style_class);
            GridPane.setMargin(event_display_tail, new Insets(0,5,0,5));
            event_display_tail.setOnMouseClicked( a -> {
                DisplayEvent(e);
            });

            event_display_tail.setMaxHeight(endtime_minutes / 60 * 16);
            event_display_tail.setPrefHeight(endtime_minutes / 60 * 16);
            // draw the remaining minutes

            //making the minutes look pretty
            GridPane.setFillHeight(event_display_tail, false);
            GridPane.setValignment(event_display_tail, VPos.TOP);

            // add minutes to calendar
            calendargrid.add(event_display_tail, DayofWeek , Integer.parseInt(e.getStarttime().substring(0, e.getStarttime().length() - 2)) + 1 + count);
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

    /**
     * draws the AddEventPage and gets the user info
     */
    @FXML
    private void AddEvent() {
        CalendarPage.setVisible(false);
        AddEventPage.setVisible(true);
        AddEventPage.toFront();

        // need to clear items everytime in case this page has previously been called this sesison

        subject.clear();
        starttime.clear();
        endtime.clear();

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
            int year, month, day;

            if(datepicker.getValue() == null){
                year = 0;
                month = 0;
                day = 0;
            }
            else{
                year = datepicker.getValue().getYear();
                month = datepicker.getValue().getMonthValue();
                day = datepicker.getValue().getDayOfMonth();
            }

            boolean valid_info;

            valid_info = new_event.VerifyEventData(year, month, day,
                    subject.getText(), occur.getValue(), starttime.getText(), endtime.getText(), st_am_or_pm.getValue().equals("AM")
                    , et_am_or_pm.getValue().equals("AM"), category.getValue());


            if(valid_info){
                events.add(new_event);
                SaveState.Save(events_filepath, events); // saves object
                goBack();
            }
        });
    }

    /**
     * Draws the removeevent page and gets user info
     */
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
                Events event = Events_list.getSelectionModel().getSelectedItem();
                if(event != null){
                    events.remove(event);
                    Events_list.getItems().remove(event);
                    SaveState.Save(events_filepath, events); // saves object
                }
            });
        }
    }

    /**
     * display user info in a new window
     * @param event event to be displayed
     */
    private void DisplayEvent(Events event){
        Stage event_window = new Stage();

        // have to address this window before any other open window
        event_window.initModality(Modality.APPLICATION_MODAL);
        event_window.setTitle("Event info");

        GridPane event_layout = new GridPane();
        event_layout.setPadding(new Insets(10, 10, 10, 10));
        event_layout.setHgap(7);
        event_layout.setVgap(7);
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
        if(Integer.parseInt(event.getStarttime()) >= 1200){ //check at 1200
            st_am_or_pm.setValue("PM");
            int start_time_pm = Integer.parseInt(event.getStarttime()) - 1200;
            if(start_time_pm < 100){ // for 12-1 pm
                start_time_pm = start_time_pm + 1200;
            }
            starttime.setText(String.format("%04d", start_time_pm));
        }
        else{
            st_am_or_pm.setValue("AM");
            if(Integer.parseInt(event.getStarttime()) < 100){ // for 12-1 am
                int temp = Integer.parseInt(event.getStarttime()) + 1200;
                starttime.setText(String.format("%04d", temp));
            }
            else{
                starttime.setText(event.getStarttime());
            }
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
            if(Integer.parseInt(event.getEndtime()) < 100){ // for 12-1 am
                int temp = Integer.parseInt(event.getEndtime()) + 1200;
                endtime.setText(String.valueOf(temp));
            }
            else{
                endtime.setText(event.getEndtime());
            }
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

    /**
     * draws  the ReminderPage and collects user info
     */
    @FXML
    private void AddReminder() {
        CalendarPage.setVisible(false);
        AddReminderPage.setVisible(true);
        AddReminderPage.toFront();

        subject_rmdr.clear();
        starttime_rmdr.clear();

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

            int year, month, day;

            if(datepicker_rmdr.getValue() == null){
                year = 0;
                month = 0;
                day = 0;
            }
            else{
                year = datepicker_rmdr.getValue().getYear();
                month = datepicker_rmdr.getValue().getMonthValue();
                day = datepicker_rmdr.getValue().getDayOfMonth();
            }

            boolean valid_info;

            valid_info = new_reminder.VerifyReminderData(year, month, day, subject_rmdr.getText(),
                    occur_rmdr.getValue(), starttime_rmdr.getText(), st_am_or_pm_rmdr.getValue().equals("AM"),
                    category_rmdr.getValue(), priority_rmdr.getValue());

            if(valid_info){
                reminders.add(new_reminder);
                SaveState.Save(reminders_filepath, reminders); // saves object
                goBack();
            }
        });
    }

    /**
     * draws RemoveReminder Page and collects user info
     */
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
                Reminders removed_reminder = Reminders_list.getSelectionModel().getSelectedItem();
                if(removed_reminder != null){
                    reminders.remove(removed_reminder);
                    Reminders_list.getItems().remove(removed_reminder);
                    SaveState.Save(reminders_filepath, reminders); // saves object
                }
            });
        }
    }

    /**
     * Displays Reminder info in a new window
     * @param reminder reminder to be displayed
     */
    private void DisplayReminder(Reminders reminder){
        Stage reminder_window = new Stage();

        // have to address this window before any other open window
        reminder_window.initModality(Modality.APPLICATION_MODAL);
        reminder_window.setTitle("Reminder info");

        GridPane reminder_layout = new GridPane();
        reminder_layout.setPadding(new Insets(10, 10, 10, 10));
        reminder_layout.setHgap(7);
        reminder_layout.setVgap(7);
        //reminder_layout.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
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
        if(Integer.parseInt(reminder.getStarttime()) >= 1200){ //check at 1200
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


    //created by Tyler Chow

    /**
     *  For the searchable interface displays what is searched
     * @param searchTerm Item that is being searched
     * @return String of search items
     */
    public static String Search(String searchTerm) {
        String results = "";
        boolean foundSomething = false;

        //making sure events and reminders were loaded
        if (events.isEmpty()){events = SaveState.Load(CalendarController.events_filepath, Events.class);}
        if (reminders.isEmpty()){reminders = SaveState.Load(CalendarController.reminders_filepath, Reminders.class);}


        //searching the events for searchTerm
        int eventIndex = 0;
        boolean foundEvent = false;

        while (eventIndex < events.size()){
            Events currentEvent = events.get(eventIndex);

            //found an event with searchTerm
            if (currentEvent.getSubject().contains(searchTerm)){

                //adding the title if it hasn't already
                if (!foundEvent){
                    results = results + "Events";
                }


                //adding the event information
                results = results + "\n\n     ";
                results = results + currentEvent.getSubject();
                results = results + "\n     " + currentEvent.getCategory();

                //adding the date and changing its format
                ZonedDateTime tempDate = currentEvent.getDate();
                results = results + "\n     " + tempDate.getDayOfWeek().name().substring(0, 1) + tempDate.getDayOfWeek().name().substring(1).toLowerCase() + ", " +
                        tempDate.getMonth().name().substring(0, 1) + tempDate.getMonth().name().substring(1).toLowerCase() + " " +
                        tempDate.getDayOfMonth();

                results = results + "\n     " + currentEvent.getStarttime();
                results = results + "\n     " + currentEvent.getEndtime();



                foundSomething = true;
                foundEvent = true;
            }

            eventIndex += 1;
        }



        //searching the reminders for searchTerm
        int reminderIndex = 0;
        boolean foundReminder = false;

        while (reminderIndex < reminders.size()){
            Reminders currentReminder = reminders.get(reminderIndex);

            //found a reminder with searchTerm
            if (currentReminder.getSubject().contains(searchTerm)){

                //adding the title if it hasn't already

                if (!foundReminder && foundEvent){
                    results = results + "\n\n\nReminders";//adding space between sections
                }
                else if (!foundEvent){
                    results = results + "Reminders";
                }


                //adding the reminder information
                results = results + "\n\n     ";
                results = results + currentReminder.getSubject();
                results = results + "\n     " + currentReminder.getCategory();
                results = results + "\n     " + currentReminder.getPriorityLevel();

                //adding the date and changing its format
                ZonedDateTime tempDate = currentReminder.getDate();
                results = results + "\n     " + tempDate.getDayOfWeek().name().substring(0, 1) + tempDate.getDayOfWeek().name().substring(1).toLowerCase() + ", " +
                        tempDate.getMonth().name().substring(0, 1) + tempDate.getMonth().name().substring(1).toLowerCase() + " " +
                        tempDate.getDayOfMonth();
                results = results + "\n     " + currentReminder.getStarttime();


                foundSomething = true;
                foundReminder = true;
            }

            reminderIndex += 1;
        }

        if (foundSomething){return results;}

        return searchErrorMsg;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Load in Events and Reminders
        events = SaveState.Load(events_filepath, Events.class);
        reminders = SaveState.Load(reminders_filepath, Reminders.class);

        // Schedule Reminders
        for (Reminders reminder: reminders){
            reminder.schedule();
        }

        goBack(); // this returns to the calendar view
    }
}
