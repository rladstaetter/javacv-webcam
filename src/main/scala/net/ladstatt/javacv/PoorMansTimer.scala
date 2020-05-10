package net.ladstatt.javacv

/**
 * super simple time measuring for runtime of f
 */
object PoorMansTimer {

  var s = 0L
  var cnt = 1L

  def reset(): Unit = {
    s = 0L
    cnt = 1L
  }

  def time[A](f: => A): Unit = {
    val before = System.nanoTime
    f
    s += (System.nanoTime - before)
    if (cnt % 100 == 0) {
      println(s"Mean: ${s / cnt}")
    }
    cnt += 1
  }
}
