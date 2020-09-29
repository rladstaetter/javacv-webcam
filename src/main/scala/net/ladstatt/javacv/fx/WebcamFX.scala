package net.ladstatt.javacv.fx

import javafx.application.Application
import javafx.fxml.{FXMLLoader, JavaFXBuilderFactory}
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import zio.Runtime

import scala.util.{Failure, Success, Try}

/**
 * Shows two different ways to display a webcam image stream with JavaFX
 */
class WebcamFX extends Application {

  /** only one zio runtime per application */
  val runtime: Runtime[zio.ZEnv] = Runtime.default

  val DirectBufferFXML = "/net/ladstatt/javacv/fx/directbuffer.fxml"

  def mkFxmlLoader(fxmlResource: String): FXMLLoader = {
    val location = getClass.getResource(fxmlResource)
    require(location != null, s"Could not resolve $fxmlResource: Location was null.")
    val fxmlLoader = new FXMLLoader()
    fxmlLoader.setLocation(location)
    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory())
    fxmlLoader
  }

  override def start(stage: Stage): Unit =
    Try {
      stage.setTitle("WebcamFX")
      val fxmlLoader = mkFxmlLoader(DirectBufferFXML)
      val parent = fxmlLoader.load[BorderPane]()
      val controller = fxmlLoader.getController[WebcamFXController]

      stage.setScene(new Scene(parent))

      stage.setOnShown(_ => {
        controller.setZioRuntime(runtime)
      })
      stage.setOnCloseRequest(_ => {
        controller.shutdown()
        stage.close()
      })
      stage.show()

    } match {
      case Success(_) =>
      case Failure(e) =>
        e.printStackTrace()
        System.err.println("Could not initialize WebcamFX application.")
    }

}
