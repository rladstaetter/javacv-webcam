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

  protected def updateView(frame: Frame): Unit = {
    val w = frame.imageWidth
    val h = frame.imageHeight

    val mat = javaCVConv.convert(frame)
    val javCVMat = new Mat
    cvtColor(mat, javCVMat, COLOR_BGR2BGRA)

    val buffer: ByteBuffer = javCVMat.createBuffer()
    val pb = new PixelBuffer(w, h, buffer, formatByte)
    val wi = new WritableImage(pb)
    videoView.setImage(wi)
  }

}
