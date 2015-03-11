/**
 * Created by root on 12/15/14.
 */
object Qsort {

  val swapFunc = (arr: Array[Int], l: Int, r: Int) => { val temp = arr(l); arr(l) = arr(r); arr(r) = temp }

  def splitBy(arr: Array[Int], start: Int, end: Int): Int = {
    var left = start; var right = end - 1; val pivot = arr(start)
    while (left < right) {
      while (left < right && arr(left) <= pivot) left = left + 1
      while (left < right && arr(right) > pivot) right = right - 1
      if (left != right) swapFunc(arr, left, right)
    }

    if (arr(left) <= pivot) {
      swapFunc(arr, start, left)
      left
    }
    else {
      swapFunc(arr, start, left - 1)
      left - 1
    }
  }

  def qsort(arr: Array[Int], start: Int, end: Int): Array[Int] = {
    if (start < end - 1) {
      val split = splitBy(arr, start, end)
      qsort(arr, start, split)
      qsort(arr, split + 1, end)
    }
    arr
  }

  val input: Array[Array[Int]] = Array(
    Array[Int](),
    Array(1),
    Array(1, 2),
    Array(2, 1),
    (1 to 10).toArray,
    (10 to (1, -1)).toArray,
    Array(1, 10, 3, 8, 5, 6, 7, 4, 9, 2),
    Array(2, 9, 4, 7, 6, 5, 8, 3, 10, 1),
    Array(5, 4, 3, 2, 1, 1, 2, 3, 4, 5)
  )

  def main(args: Array[String]): Unit = {
    for (arr <- input) {
      println(arr.mkString(" ") + "\n" + "-> " + qsort(arr, 0, arr.length).mkString(" ") + "\n")
    }
  }
}
