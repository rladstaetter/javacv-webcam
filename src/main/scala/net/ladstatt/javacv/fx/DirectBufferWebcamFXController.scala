package net.ladstatt.javacv.fx


import java.nio.ByteBuffer

import javafx.scene.image._
import org.bytedeco.javacv.Frame
import org.bytedeco.opencv.global.opencv_imgproc._
import org.bytedeco.opencv.opencv_core.Mat


/**
 * Directly use native allocated byte array in JavaFX ImageView
 */
class DirectBufferWebcamFXController extends WebcamFXController {

  val javaCVMat = new Mat

  /** create buffer only once saves much time! */
  lazy val buffer: ByteBuffer = javaCVMat.createBuffer()

  protected def updateView(frame: Frame): Unit = {
    val w = frame.imageWidth
    val h = frame.imageHeight

    val mat = javaCVConv.convert(frame)
    cvtColor(mat, javaCVMat, COLOR_BGR2BGRA)

    val pb = new PixelBuffer(w, h, buffer, formatByte)
    val wi = new WritableImage(pb)
    videoView.setImage(wi)
  }

}
