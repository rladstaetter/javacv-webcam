# javacv-webcam

A simple comparison between 3 different approaches to use a webcam with JavaCV:

- a swing based approach (official endorsed)
- JavaFX using a JavaFXOpenCV Converter
- JavaFX with PixelBuffer backed by a native array

It turns out that the third approach is the fastest one.

You will need at least [Java11](https://adoptopenjdk.net) and [Maven 3.6.2](https://maven.apache.org/) to build.

Try it out yourself by cloning this repository, then:

    mvn package
  
It will take some time until it is finished, especially when run the first time. After that, you'll find two jar files in the target directory.

Again, on the command line, enter:

    java -jar target/javacv-webcam-2020.2.1-SNAPSHOT-jar-with-dependencies.jar
    
A window should appear and chances are high you can see yourself on the monitor.

