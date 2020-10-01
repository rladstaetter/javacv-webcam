module net.ladstatt.javacv.webcam {
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.bytedeco.javacv;
    requires org.bytedeco.opencv;
    requires org.bytedeco.opencv.macosx.x86_64;
    // uncomment for windows
    //requires org.bytedeco.opencv.windows.x86_64;

    // uncomment for linux
    //requires org.bytedeco.opencv.linux.x86_64;

    exports net.ladstatt.javacv.fx to javafx.graphics, javafx.fxml;
    opens net.ladstatt.javacv.fx to javafx.fxml;
}