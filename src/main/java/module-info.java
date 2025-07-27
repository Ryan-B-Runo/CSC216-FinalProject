module com.example.csc216finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.csc216finalproject to javafx.fxml;
    exports com.example.csc216finalproject;
}