package net.ladstatt.javacv

import net.ladstatt.javacv.fx.WebcamFXApp
import net.ladstatt.javacv.swing.WebcamSwingApp

/**
 * Starts either Swing Application which shows a webcam image stream, or a JavaFX based
 * implementation.
 *
 * - args(0) == 'swing' then show Swing variant
 * - args(0) == 'classic' show classic JavaFX variant
 * - in all other cases show JavaFX DirectBuffer variant.
 *
 */
object WebcamComparisonApp {

  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      WebcamFXApp.main(args)
    } else {
      if (args(0) == "swing") {
        WebcamSwingApp.main(args)
      } else {
        WebcamFXApp.main(args)
      }
    }
  }

}
