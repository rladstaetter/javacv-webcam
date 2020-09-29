package net.ladstatt.javacv.fx

import java.net.URL
import java.util.ResourceBundle

import javafx.beans.property.{SimpleBooleanProperty, SimpleObjectProperty}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.image._
import net.ladstatt.javacv.PoorMansTimer
import org.bytedeco.javacv.{Frame, OpenCVFrameConverter, OpenCVFrameGrabber}
import zio._

abstract class WebcamFXController extends Initializable {

  val zioRuntimeProperty = new SimpleObjectProperty[Runtime[zio.ZEnv]]()

  @FXML var videoView: ImageView = _

  def setZioRuntime(runtime: Runtime[zio.ZEnv]): Unit = zioRuntimeProperty.set(runtime)

  def getZioRuntime(): Runtime[zio.ZEnv] = zioRuntimeProperty.get()

  protected def updateView(frame: Frame): Unit

  val javaCVConv = new OpenCVFrameConverter.ToMat

  /** controls if application closes */
  val cameraActiveProperty = new SimpleBooleanProperty(true)

  def setCameraActive(isActive: Boolean): Unit = cameraActiveProperty.set(isActive)

  def getCameraActive: Boolean = cameraActiveProperty.get

  def shutdown(): Unit = setCameraActive(false)

  val frameGrabber: OpenCVFrameGrabber = {
    val grabber = new OpenCVFrameGrabber(0)
    grabber.start()
    grabber
  }

  /*
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

    */
  def setVideoView(mat: Frame): Unit = {
    PoorMansTimer.time(updateView(mat))
  }

  val loop = for {
    frame <- ZIO.succeed(frameGrabber.grab())
    _ <- ZIO.succeed(setVideoView(frame))
  } yield ()


  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    zioRuntimeProperty.addListener(new ChangeListener[Runtime[ZEnv]] {
      override def changed(observableValue: ObservableValue[_ <: Runtime[zio.ZEnv]], t: Runtime[zio.ZEnv], t1: Runtime[zio.ZEnv]): Unit = {
        Option(t1) match {
          case Some(rt) => new Thread(() => rt.unsafeRun(loop.forever)).start()
          case None =>
        }
      }
    }

    )
    // videoSource.left
    //    getZioRuntime().unsafeRun(Task(println("Hello World!")))
    /*
    videoObservable.subscribe(
      (frame: Frame) => setVideoView(frame)
      , t => t.printStackTrace()
      , () => println("Videostream stopped..."))
    ( )

   */
  }

}
