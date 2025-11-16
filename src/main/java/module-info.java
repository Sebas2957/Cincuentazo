module org.example._0zo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires junit;


    opens org.example._0zo to javafx.fxml;
    exports org.example._0zo;
    exports org.example._0zo.Controller;
    opens org.example._0zo.Controller to javafx.fxml;
}