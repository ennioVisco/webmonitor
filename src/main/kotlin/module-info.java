module webmonitor {
    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.web;

    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires io.github.bonigarcia.webdrivermanager;

    requires io.github.oshai.kotlinlogging;
    requires moonlight.engine;

    requires kotlin.stdlib;
    requires java.scripting;

    exports com.enniovisco;

    opens com.enniovisco to javafx.graphics;
}
