module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.pdfbox;
    //requires gson;

    opens com.example.demo to javafx.fxml, com.google.gson, org.apache.box;

    exports com.example.demo;
    exports com.example.demo.Controller;
    opens com.example.demo.Controller to com.google.gson, javafx.fxml, org.apache.box;
    exports com.example.demo.UserInput;
    opens com.example.demo.UserInput to com.google.gson, javafx.fxml, org.apache.box;
    exports com.example.demo.Model;
    opens com.example.demo.Model to com.google.gson, javafx.fxml, org.apache.box;
}