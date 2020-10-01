package net.ladstatt.javacv.fx;


import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2BGRA;

public class WebcamFXController implements Initializable {

    @FXML
    ImageView videoView;

    Mat javaCVMat = new Mat();

    /**
     * create buffer only once saves much time!
     */
    WritablePixelFormat<ByteBuffer> formatByte = PixelFormat.getByteBgraPreInstance();

    OpenCVFrameConverter<Mat> javaCVConv = new OpenCVFrameConverter.ToMat();

    /**
     * controls if application closes
     */
    SimpleBooleanProperty cameraActiveProperty = new SimpleBooleanProperty(true);

    OpenCVFrameGrabber frameGrabber = new OpenCVFrameGrabber(0);

    ByteBuffer buffer;

    protected void updateView(Frame frame) {
        int w = frame.imageWidth;
        int h = frame.imageHeight;

        Mat mat = javaCVConv.convert(frame);
        opencv_imgproc.cvtColor(mat, javaCVMat, COLOR_BGR2BGRA);
        if (buffer == null) {
            buffer = javaCVMat.createBuffer();
        }

        PixelBuffer<ByteBuffer> pb = new PixelBuffer<ByteBuffer>(w, h, buffer, formatByte);
        final WritableImage wi = new WritableImage(pb);
        Platform.runLater(() -> videoView.setImage(wi));

    }


    public void setCameraActive(Boolean isActive) {
        cameraActiveProperty.set(isActive);
    }

    public Boolean getCameraActive() {
        return cameraActiveProperty.get();
    }

    public void shutdown() {
        setCameraActive(false);
    }

    void setVideoView(Frame mat) {
        updateView(mat);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            frameGrabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            while (getCameraActive()) {
                try {
                    Frame frame = frameGrabber.grab();
                    setVideoView(frame);
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                frameGrabber.release();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
