package net.ladstatt.javacv.fx

import org.bytedeco.javacv.{Frame, JavaFXFrameConverter}


/**
 * Classic variant to convert Frames to JavaFX Images and then display them
 */
class JavaFXConverterWebcamFXController extends WebcamFXController {

  val jfxFrameConverter = new JavaFXFrameConverter

  override protected def updateView(frame: Frame): Unit = {
    videoView.setImage(jfxFrameConverter.convert(frame))
  }

}
