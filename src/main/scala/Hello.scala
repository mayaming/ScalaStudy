/**
 * Created by root on 12/3/14.
 */
object Hello {
  def main(args:Array[String]): Unit = {
    println("Hello, scala")

    //Here "1 to 10" will create a RichInt(1) first; the RichInt class has a method called "to",
    //which returns a Range.Inclusive to represent range from 1 to 10 (both inclusive).
    //Note that in Scala method call like "inst.func(param)" could be written as "inst func param".
    println(1 to 10)  //Output: Range(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    //For "until" method the end element is exclusive
    println(1 until 10)  //Output: Range(1, 2, 3, 4, 5, 6, 7, 8, 9)

    //Specify the number to increase by for each element in this range
    //Note that if you have more than 1 parameter, group them in a pair of parentheses for compiler
    //to recognize them correctly. Otherwise in this example (1 to 10, 2) would be considered to be
    //a list with 2 elements: a Range.Inclusive from "1 to 10" and an Int 2.
    println(1 to (10, 2))  //Output: Range(1, 3, 5, 7, 9)

    //Default step is 1, so you can't reach 1 by continuously adding 1 to 10. The range is empty.
    println(10 to 1)  //Output: Range()

    //But you can "increase" 10 to 1 by adding "-1" to it.
    println(10 to (1, -1))  //Output: Range(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
  }
}
