package net.ladstatt.javacv.fx

import java.net.URL
import java.nio.ByteBuffer
import java.util.ResourceBundle

import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.{FXML, Initializable}
import javafx.scene.image._
import net.ladstatt.javacv.PoorMansTimer
import org.bytedeco.javacv.{Frame, OpenCVFrameConverter, OpenCVFrameGrabber}
import rx.lang.scala.Observable

import scala.util.{Failure, Success, Try}

abstract class WebcamFXController extends Initializable {

  @FXML var videoView: ImageView = _

  protected def updateView(frame: Frame): Unit

  val javaCVConv = new OpenCVFrameConverter.ToMat

  val cameraActiveProperty = new SimpleBooleanProperty(true)

  def setCameraActive(isActive: Boolean): Unit = cameraActiveProperty.set(isActive)

  def getCameraActive: Boolean = cameraActiveProperty.get

  def shutdown(): Unit = setCameraActive(false)

  val frameGrabber: OpenCVFrameGrabber = {
    val grabber = new OpenCVFrameGrabber(0)
    grabber.start()
    grabber
  }

  val videoObservable: Observable[Frame] =
    Observable[Frame](o => {
      new Thread(
        () => {
          while (getCameraActive) {
            Try {
              frameGrabber.grab()
            } match {
              case Success(m) => o.onNext(m)
              case Failure(e) => o.onError(e)
            }
          }
          frameGrabber.release()
          o.onCompleted()
        }).start()
    })

  def setVideoView(mat: Frame): Unit = {
    PoorMansTimer.time(updateView(mat))
  }



  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    videoObservable.subscribe(
      (frame: Frame) => setVideoView(frame)
      , t => t.printStackTrace()
      , () => println("Videostream stopped..."))
  }
}
