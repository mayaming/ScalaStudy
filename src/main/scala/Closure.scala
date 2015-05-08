/**
 * After running "scalac Closure.scala", you'll get the following classes for this piece of code:
 * Outmost.class
 * Outmost$Outer.class
 * Outmost$Outer$Inner.class
 * Outmost$Outer$Inner$Innest.class
 * Outmost$Outer$Inner$$anonfun$innerF$1.class
 * Outmost$Outer$Inner$$anonfun$innerF2$1.class
 * Outmost$Outer$Inner$ClsInF$1.class
 * Outmost$Outer$Inner$ClsInF$2.class
 */

class Outmost {

  class Outer {

    def outerF(a: Any) = a

    class Inner {

      def innerF(l: Seq[Int]) = {
        class ClsInF(n: Int) {
        }
        //Outmost$Outer$Inner$$anonfun$innerF$1
        l.map(new ClsInF(_))
      }

      def innerF2(l: Seq[Int]) = {
        class ClsInF(n: Int) {
        }
        //Outmost$Outer$Inner$$anonfun$innerF2$1
        l.map(outerF)

        //Outmost$Outer$Inner$$anonfun$1
        val f: Any => Any = outerF
        println(f.toString())  //-> <function1>
        println(f.getClass())  //-> Outmost$Outer$Inner$$anonfun$1
        println(f.getClass().getSuperclass())  //-> class scala.runtime.AbstractFunction1
        println(f.getClass().getInterfaces().mkString(","))  //-> interface scala.Serializable
        l.map(f)
      }

      class Innest {
      }

    }

  }

}

class Capture {
  var n: Int = 0

  def makeClosure() = { () => { n += 1; println(n) }}

  def test(): Unit = {
    val arr = new Array[()=>Unit](2)
    (0 until arr.length).foreach(arr(_) = makeClosure())
    //Capture by reference
    //1
    //2
    arr.foreach(_())
  }
}

object Closure {
  def main(args: Array[String]):Unit = {
    val outmost = new Outmost
    val outer = new outmost.Outer
    val inner = new outer.Inner
    inner.innerF(0 to 9)
    inner.innerF2(0 to 9)

    val c = new Capture
    c.test()
    println(c.n)
  }
}