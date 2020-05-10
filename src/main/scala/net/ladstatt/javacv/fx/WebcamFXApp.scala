package net.ladstatt.javacv.fx

import javafx.application.Application

/**
 * Shows a minimal JavaFX application which uses JavaCV (and OpenCV under the hood)
 * to display a webcam image stream on your display.
 *
 * Application uses per default 'DirectBuffer' strategy, if you want to compare it to
 * the classic JavaFXConverter approach provide one parameter called '
 */
object WebcamFXApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[WebcamFX], args: _*)
  }

}
