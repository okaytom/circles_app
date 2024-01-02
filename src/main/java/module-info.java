module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.pdfbox;
    requires Spire.Pdf;
    requires java.desktop;

    opens com.example.demo to javafx.fxml, com.google.gson, org.apache.box;

    exports com.example.demo;
}