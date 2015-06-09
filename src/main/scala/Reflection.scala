import scala.reflect.runtime.universe._

import scala.reflect.ClassTag
/**
 * Created by root on 6/8/15.
 */
object Reflection {

  /*
  This one does not work because it has not class tag
  def mkArrayBad[T](elems: T*) = Array(elems: _*)
  */

  def mkArray1[T: ClassTag](elems: T*) = Array[T](elems: _*)

  def showType1[T](elem: T): Unit = {
    println("Element value is " + elem.toString)
    //Won't work --> mkArray1(elem)
    //Won't work --> println("Class value is " + classOf[T])
  }

  def showType2[T: ClassTag](elem: T): Unit = {
    println("Element value is " + elem.toString)
    mkArray1(elem)
    //Won't work --> println("Class value is " + classOf[T])
  }

  def showType3[T](elem: T)(implicit tag: ClassTag[T]): Unit = {
    mkArray1(elem)
    println("Class is " + tag.runtimeClass)
  }

  def showType4[T: TypeTag](elem: T): Unit = {
    val tags = typeOf[T] match {
      case TypeRef(_, _, args) => args
    }
    //Won't work --> mkArray1(elem)
    println("Class is " + tags)
  }

  def main(args: Array[String]) = {

    //int[]
    //java.lang.String[]
    println(mkArray1(10, 20).getClass.getCanonicalName)
    println(mkArray1("hello", "world").getClass.getCanonicalName)

    //Element value is hello
    //Element value is 1
    //Element value is List(1, 2)
    showType1("hello")
    showType1(1)
    showType1(List(1, 2))

    //Element value is hello
    //Element value is 1
    //Element value is List(1, 2)
    showType2("hello")
    showType2(1)
    showType2(List(1, 2))

    //Class is class java.lang.String
    //Class is int
    //Class is class scala.collection.immutable.List
    showType3("hello")
    showType3(1)
    showType3(List(1, 2))

    //Class is List()
    //Class is List()
    //Class is List(Int)
    showType4("hello")
    showType4(1)
    showType4(List(1, 2))
  }
}
