module matt.hurricanefx {

    requires kotlin.stdlib.jdk8;
    requires kotlin.stdlib.jdk7;
    requires kotlin.reflect;



    requires transitive javafx.controls;
    requires transitive matt.hurricanefx.eye;

    requires java.desktop;
    requires javafx.web;
    requires javafx.media;

    exports matt.tornadofx;
    exports matt.hurricanefx;
}