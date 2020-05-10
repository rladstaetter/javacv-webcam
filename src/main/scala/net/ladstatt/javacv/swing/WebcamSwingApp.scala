package net.ladstatt.javacv.swing

import net.ladstatt.javacv.PoorMansTimer
import org.bytedeco.javacv._

/**
 * Shows how to use JavaCV and OpenCV for a simple webcam functionality
 *
 * Checkout this marvelous project: https://github.com/bytedeco/javacv
 *
 * This file is based on the main example and a simple port to Scala. It should show what
 * is needed with JavaCV to display a webcam image on your computer display.
 *
 */
object WebcamSwingApp {

  def main(args: Array[String]): Unit = {

    val grabber = new OpenCVFrameGrabber(0)
    grabber.start()
    val canvasFrame = new CanvasFrame("WebCam Swing App", CanvasFrame.getDefaultGamma / grabber.getGamma)
    while (true) {
      PoorMansTimer.time(canvasFrame.showImage(grabber.grab))
    }
    canvasFrame.dispose()
    grabber.stop()
  }


}