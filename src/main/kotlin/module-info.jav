module webmonitor {
    // JavaFX
    requires javafx.base;
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.web;

    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires io.github.bonigarcia.webdrivermanager;

    requires io.github.microutils.kotlinlogging;
//    requires moonlight;
    requires kotlin.stdlib;

    exports com.enniovisco;

    opens com.enniovisco to javafx.graphics;
}
