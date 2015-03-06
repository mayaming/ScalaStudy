/**
 * Created by root on 1/6/15.
 */

abstract class BSTree {
  def contains(n: Integer): Boolean
  def add(n: Integer): BSTree
}

case object EmptyBSTree extends BSTree {
  def contains(n: Integer) = false
  def add(n: Integer): BSTree = {
    NodeBSTree(n, EmptyBSTree, EmptyBSTree)
  }

}

case class NodeBSTree(n: Integer, var left: BSTree, var right: BSTree) extends BSTree {
  def contains(i: Integer) = {
    if (i < n) left.contains(i)
    else if (i > n) right.contains(i)
    else true
  }
  def add(i: Integer) = {
    if (i <= n) left = left.add(i)
    else right = right.add(i)
    this
  }
}

class NonCaseBSTree extends BSTree {
  def contains(n: Integer) = true
  def add(n: Integer) = this
}

case class CustomizeToStringCase(n: Integer) {
  override def toString(): String = {
    new java.util.Date().toString;
  }
}

object CaseAndPattern {
  def printBSTree(t: BSTree): Unit = { t match {
      case EmptyBSTree => print ("_")
      case NodeBSTree(n, l, r) => print("("); printBSTree(l); print(" "); print(n); print(" "); printBSTree(r); print(")")
      //Only case class/object could act as case branch condition
      //case NonCaseBSTree => print("NonCaseBSTree")
    }
  }

  def main(args: Array[String]): Unit = {
    val t1 = EmptyBSTree add 5 add 1 add 9
    println(t1.toString)
    val t2 = EmptyBSTree.add(5).add(1).add(9)
    println(t2.toString)
    println(t1 == t2)
    printBSTree(t1)
    println("")

    val t3 = NodeBSTree(1, new NonCaseBSTree, EmptyBSTree)
    println(t3.toString)
    val t4 = NodeBSTree(1, new NonCaseBSTree, EmptyBSTree)
    println(t4.toString)
    println(t3 == t4)
    //The call below will lead to scala.MatchError
    //printBSTree(t3)

    val t5 = CustomizeToStringCase(1);
    Thread.sleep(1000)
    val t6 = CustomizeToStringCase(1);
    Thread.sleep(1000)
    val t7 = CustomizeToStringCase(2);
    println(t5.toString())
    println(t6.toString())
    println(t5 == t6)
    println(t5.equals(t6))

    println(t6.toString())
    println(t7.toString())
    println(t6 == t7)
    println(t6.equals(t7))
  }
}
