module com.enniovisco.webmonitor {
    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.graphics;
    requires javafx.web;

    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires io.github.bonigarcia.webdrivermanager;

    requires io.github.oshai.kotlinlogging;
    requires moonlight.engine;

    requires kotlin.stdlib;
    requires java.scripting;

    exports com.enniovisco;
    exports com.enniovisco.checking;
    exports com.enniovisco.cli;
    exports com.enniovisco.dsl;
    exports com.enniovisco.reporting;
    exports com.enniovisco.space;
    exports com.enniovisco.tracking;
    exports com.enniovisco.tracking.commands;

    opens com.enniovisco to javafx.graphics;
}
