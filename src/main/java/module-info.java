module com.example.hotelmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;
    requires java.desktop;

    opens com.example.hotelmanager to javafx.fxml;
    exports com.example.hotelmanager;
}