module com.example.zapoctovyprogramjava1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.zapoctovyprogramjava1 to javafx.fxml;
    exports com.example.zapoctovyprogramjava1;
}