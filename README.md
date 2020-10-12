# javacv-webcam

A project which shows how to use JavaFX together with OpenCV / JavaCV bindings.
 
This project can be used as a starting point for experiments with OpenCV and Java. It showcases the usage of the PixelBuffer API which permits a fast display / rendering of a webcam image stream with JavaFX. 

Furthermore it uses [javafx-maven-plugin](https://github.com/openjfx/javafx-maven-plugin) to generate a custom java image. 

You will need at least [Java11](https://adoptopenjdk.net) and [Maven 3.6.2](https://maven.apache.org/) to build.

## javafx-maven-plugin

For a quick glimpse of what this project does just checkout the code and enter

    mvn javafx:run
    
After downloading dependencies and compiling a window should appear with your webcam image stream.

If you want to create a custom jre distribution, enter

    mvn javafx:jlink
  
which creates a custom JDK11 based distribution. You can start the application by entering

    ./target/javacv-webcam/bin/jwebcam
    
If you peek into the ``javacv-webcam`` directory, you will see that it is about 100 MB in size, all batteries included.

Attention: If you are using Windows or Linux you'll have to adapt module-info.java to include native libraries of suitable for those platforms. On MacOsX, you may have problems with permissions to access your webcam from a java application, make sure to allow access in your system preferences.

## Links

- Have a look at [https://openjfx.io](https://openjfx.io) where you can find more information and introductory examples for developing JavaFX applications. 
- [https://github.com/bytedeco/javacv](https://github.com/bytedeco/javacv) is the home of JavaCV which enables Java Developers to use native libraries like [OpenCV](https://opencv.org).
- Some posts on [http://ladstatt.blogspot.com/](https://ladstatt.blogspot.com) may be of interest, too.


