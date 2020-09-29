module net.ladstatt.javacv.webcam {
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.bytedeco.opencv.platform;
    requires org.bytedeco.javacv.platform;
    exports net.ladstatt.javacv.fx to javafx.graphics, javafx.fxml;
    opens net.ladstatt.javacv.fx to javafx.fxml;
}