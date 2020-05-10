package net.ladstatt.javacv.fx

import javafx.application.Application
import javafx.fxml.{FXMLLoader, JavaFXBuilderFactory}
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

/**
 * Shows two different ways to display a webcam image stream with JavaFX
 */
class WebcamFX extends Application {

  val JavaFXConverterFXML = "/net/ladstatt/javacv/fx/javafxconverter.fxml"
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
      val (fxmlResource, title) =
        getParameters.getRaw.asScala.headOption match {
          case Some("classic") => (JavaFXConverterFXML, "WebcamFX with JavaFXConverter")
          case _ => (DirectBufferFXML, "WebcamFX with DirectBuffer")
        }
      stage.setTitle(title)
      val fxmlLoader = mkFxmlLoader(fxmlResource)
      val parent = fxmlLoader.load[BorderPane]()
      val controller = fxmlLoader.getController[WebcamFXController]
      val scene = new Scene(parent)

      stage.setScene(scene)

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
