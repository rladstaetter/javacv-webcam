package net.ladstatt.javacv

import java.net.URL

import org.bytedeco.javacpp._
import org.bytedeco.javacpp.indexer.{DoubleRawIndexer, FloatIndexer}
import org.bytedeco.javacv._
import org.bytedeco.opencv.global.opencv_calib3d._
import org.bytedeco.opencv.global.opencv_core._
import org.bytedeco.opencv.global.opencv_imgproc._
import org.bytedeco.opencv.opencv_core._
import org.bytedeco.opencv.opencv_objdetect._


/**
 * Port of the example given at bytedeco's github page:
 *
 * https://github.com/bytedeco/javacv
 *
 * to a scala version
 */
object SwingDemo {

  def main(args: Array[String]): Unit = {
    var classifierName: String = null
    if (args.length > 0) {
      classifierName = args(0)
    } else {
      val url = new URL("https://raw.github.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml")
      val file = Loader.cacheResource(url)
      classifierName = file.getAbsolutePath
    }
    // We can "cast" Pointer objects by instantiating a new object of the desired class.
    val classifier = new CascadeClassifier(classifierName)
    if (classifier == null) {
      System.err.println("Error loading classifier file \"" + classifierName + "\".")
      System.exit(1)
    }
    // The available FrameGrabber classes include OpenCVFrameGrabber (opencv_videoio),
    // DC1394FrameGrabber, FlyCapture2FrameGrabber, OpenKinectFrameGrabber, OpenKinect2FrameGrabber,
    // RealSenseFrameGrabber, RealSense2FrameGrabber, PS3EyeFrameGrabber, VideoInputFrameGrabber, and FFmpegFrameGrabber.
    val grabber = FrameGrabber.createDefault(0)
    grabber.start()
    // CanvasFrame, FrameGrabber, and FrameRecorder use Frame objects to communicate image data.
    // We need a FrameConverter to interface with other APIs (Android, Java 2D, JavaFX, Tesseract, OpenCV, etc).
    val converter = new OpenCVFrameConverter.ToMat
    // FAQ about IplImage and Mat objects from OpenCV:
    // - For custom raw processing of data, createBuffer() returns an NIO direct
    //   buffer wrapped around the memory pointed by imageData, and under Android we can
    //   also use that Buffer with Bitmap.copyPixelsFromBuffer() and copyPixelsToBuffer().
    // - To get a BufferedImage from an IplImage, or vice versa, we can chain calls to
    //   Java2DFrameConverter and OpenCVFrameConverter, one after the other.
    // - Java2DFrameConverter also has static copy() methods that we can use to transfer
    //   data more directly between BufferedImage and IplImage or Mat via Frame objects.
    var grabbedImage = converter.convert(grabber.grab)
    val height = grabbedImage.rows
    val width = grabbedImage.cols
    // Objects allocated with `new`, clone(), or a create*() factory method are automatically released
    // by the garbage collector, but may still be explicitly released by calling deallocate().
    // You shall NOT call cvReleaseImage(), cvReleaseMemStorage(), etc. on objects allocated this way.
    val grayImage = new Mat(height, width, CV_8UC1)
    val rotatedImage = grabbedImage.clone
    // The OpenCVFrameRecorder class simply uses the VideoWriter of opencv_videoio,
    // but FFmpegFrameRecorder also exists as a more versatile alternative.
    val recorder = FrameRecorder.createDefault("output.avi", width, height)
    recorder.start()
    // CanvasFrame is a JFrame containing a Canvas component, which is hardware accelerated.
    // It can also switch into full-screen mode when called with a screenNumber.
    // We should also specify the relative monitor/camera response for proper gamma correction.
    val frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma / grabber.getGamma)
    // Let's create some random 3D rotation...
    val randomR: Mat = new Mat(3, 3, CV_64FC1)
    val randomAxis: Mat = new Mat(3, 1, CV_64FC1)
    // We can easily and efficiently access the elements of matrices and images
    // through an Indexer object with the set of get() and put() methods.
    val Ridx = randomR.asInstanceOf[AbstractMat].createIndexer[DoubleRawIndexer]()
    val axisIdx = randomAxis.asInstanceOf[AbstractMat].createIndexer[DoubleRawIndexer]()
    axisIdx.put(0L, randDouble, randDouble, randDouble)
    Rodrigues(randomAxis, randomR)
    val f = (width + height) / 2.0
    Ridx.put(0, 2, Ridx.get(0, 2) * f)
    Ridx.put(1, 2, Ridx.get(1, 2) * f)
    Ridx.put(2, 0, Ridx.get(2, 0) / f)
    Ridx.put(2, 1, Ridx.get(2, 1) / f)
    System.out.println(Ridx)
    // We can allocate native arrays using constructors taking an integer as argument.
    val hatPoints = new Point(3)
    var x = true
    while (x) { // Let's try to detect some faces! but we need a grayscale image...
      grabbedImage = converter.convert(grabber.grab)
      x = frame.isVisible && ( grabbedImage != null)
      cvtColor(grabbedImage, grayImage, CV_BGR2GRAY)
      val faces = new RectVector
      classifier.detectMultiScale(grayImage, faces)
      val total = faces.size.toInt
      for (i <- 0 until total) {
        val r = faces.get(i)
        val x = r.x
        val y = r.y
        val w = r.width
        val h = r.height
        rectangle(grabbedImage, new Point(x, y), new Point(x + w, y + h), AbstractScalar.RED, 1, CV_AA, 0)
        // To access or pass as argument the elements of a native array, call position() before.
        hatPoints.position(0).x(x - w / 10).y(y - h / 10)
        hatPoints.position(1).x(x + w * 11 / 10).y(y - h / 10)
        hatPoints.position(2).x(x + w / 2).y(y - h / 2)
        fillConvexPoly(grabbedImage, hatPoints.position(0), 3, AbstractScalar.GREEN, CV_AA, 0)
      }
      // Let's find some contours! but first some thresholding...
      threshold(grayImage, grayImage, 64, 255, CV_THRESH_BINARY)
      // To check if an output argument is null we may call either isNull() or equals(null).
      val contours = new MatVector
      findContours(grayImage, contours, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE)
      val n = contours.size.toInt
      for (i <- 0 until n) {
        val contour = contours.get(i)
        val points = new Mat
        approxPolyDP(contour, points, arcLength(contour, true) * 0.02, true)
        drawContours(grabbedImage, new MatVector(points), -1, AbstractScalar.BLUE)
      }
      warpPerspective(grabbedImage, rotatedImage, randomR, rotatedImage.size)
      val rotatedFrame = converter.convert(rotatedImage)
      frame.showImage(rotatedFrame)
      recorder.record(rotatedFrame)


    }
    frame.dispose()
    recorder.stop()
    grabber.stop()
  }

  private def randDouble: Double = {
    (Math.random - 0.5) / 4
  }

}