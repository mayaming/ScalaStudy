/**
 * Created by root on 12/4/14.
 */
object Functional {
  def fsum(f: Int => Int) (a: Int, b: Int): Int =
    if (a > b) 0 else f(a) + fsum(f)(a+1, b)

  def isum = fsum(n => n)(_: Int, _: Int)
  def ssum = fsum(n => n*n)(_: Int, _: Int)
  def udsum = fsum(_: Int => Int)(1, 5)

  def main(args: Array[String]) {
    println("Sum of 1~5 with fsum directly: " + fsum(n => n)(1, 5))
    println("Sum of squares of 1~5 with fsum directly: " + fsum(n => n * n)(1, 5))
    println("Sum of 1~5 with isum: " + isum(1, 5))
    println("Sum of 1~5 with ssum: " + ssum(1, 5))
    println("Sum of 1~5 with udsum: " + udsum(n => n))
    println("Sum of squares of 1~5 with udsum: " + udsum(n => n*n))
  }
}
