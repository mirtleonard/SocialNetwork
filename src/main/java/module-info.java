module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml.crypto;
    requires java.sql;

    opens social_network to javafx.fxml;
    exports social_network;
    exports social_network.controller;
    opens social_network.controller to javafx.fxml;
    opens social_network.domain to javafx.base;
}