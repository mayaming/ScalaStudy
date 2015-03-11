import java.util.ArrayList
import java.util.Iterator

/**
 * Created by root on 12/9/14.
 */
trait ForEachAble[T] {
  def iterator: Iterator[T]
  def foreach(f: T => Unit) = {
    val iter = iterator
    while (iter.hasNext)
      f(iter.next())
  }
}

object TraitsExample {
  def main(args: Array[String]): Unit = {
    val list = new ArrayList[Int]() with ForEachAble[Int]
    list.add(1)
    list.add(2)

    println("For each: ")
    list.foreach(x => println(x))
  }
}