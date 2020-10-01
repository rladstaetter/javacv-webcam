package net.ladstatt.javacv.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Shows a minimal JavaFX application which uses JavaCV (and OpenCV under the hood)
 * to display a webcam image stream on your display.
 * <p>
 * Application uses per default 'DirectBuffer' strategy, if you want to compare it to
 * the classic JavaFXConverter approach provide one parameter called '
 */
public class WebcamFXApp extends Application {

    String fxml = "/net/ladstatt/javacv/fx/webcam.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(final Stage stage) throws Exception {
        stage.setTitle("WebcamFX");
        FXMLLoader fxmlLoader = mkFxmlLoader(fxml);
        Parent parent = fxmlLoader.load();
        final WebcamFXController controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));

        stage.setOnCloseRequest(windowEvent -> {
            controller.shutdown();
            stage.close();
        });

        stage.show();
    }

    private FXMLLoader mkFxmlLoader(String fxmlResource) {
        URL location = this.getClass().getResource(fxmlResource);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        return fxmlLoader;
    }

}
