# Spark中ClassTag的一个用法探究

@(Scala)[spark|反射|ClassTag|范型]

在阅读spark源码时看到下面一段内容：
```scala
class RangePartitioner[K : Ordering : ClassTag, V]
```

其中**[K : Ordering : ClassTag]**这样的语法结构头一次见到，很奇怪它的作用究竟是什么。

首先，**[K : ClassTag]**这种用法并不新鲜，在Scala ClassTag这个trait的注释中已经给了主要的使用场景：
>  scala> def mkArray[T : ClassTag](elems: T*) = Array[T](elems: _*)
>  mkArray: [T](elems: T*)(implicit evidence$1: scala.reflect.ClassTag[T])Array[T]
>
>  scala> mkArray(42, 13)
>  res0: Array[Int] = Array(42, 13)
>
>  scala> mkArray("Japan","Brazil","Germany")
>  res1: Array[String] = Array(Japan, Brazil, Germany)

可见[T: ClassTag]的作用是让编译器生成一个implicit parameter来保存编译时获得的类型参数，否则该信息会因为类型擦除而丢失。这一点在scala[关于反射](http://docs.scala-lang.org/overviews/reflection/typetags-manifests.html)的文档中也有提及。

下面以一个简短的例子解释这个implicit parameter是怎么工作的：
```scala
import scala.reflect._

object Test {
  mkArray(42, 13)
  mkArray("Japan","Brazil","Germany")

  def mkArray[T : ClassTag](elems: T*) = Array[T](elems: _*)

  def copiedArrayApply[T: ClassTag](xs: T*): Array[T] = {
    val array = new Array[T](xs.length)
    var i = 0
    for (x <- xs.iterator) { array(i) = x; i += 1 }
    array
  }
}
```

这个例子包括了上面说的mkArray的定义和调用，还有一个copiedArrayApply方法，其实就是把Array.apply拷过来，看看这些调用去”糖“以后是什么样子的：
```scala
  object Test extends scala.AnyRef {
    def <init>(): Test.type = {
      Test.super.<init>();
      ()
    };
    Test.this.mkArray[Int](42, 13)((ClassTag.Int: scala.reflect.ClassTag[Int]));
    Test.this.mkArray[String]("Japan", "Brazil", "Germany")((ClassTag.apply[String](classOf[java.lang.String]): scala.reflect.ClassTag[String]));
    def mkArray[T](elems: T*)(implicit evidence$1: scala.reflect.ClassTag[T]): Array[T] = scala.Array.apply[T]((elems: _*))(evidence$1);
    def copiedArrayApply[T](xs: T*)(implicit evidence$2: scala.reflect.ClassTag[T]): Array[T] = {
      val array: Array[T] = evidence$2.newArray(xs.length);
      var i: Int = 0;
      xs.iterator.foreach[Unit](((x: T) => {
        array.update(i, x);
        i = i.+(1)
      }));
      array
    }
  }
```

简单来说，这个过程是这样子的：
1.  按前所述，编译器会在mkArray的签名中增加一个implicit parameter来携带编译期T的具体类型。
2.  调用mkArray时，如mkArray(42, 13)，编译器会在调用语句上加一个ClassTag[Int]值来携带编译期的Int这个类型。这个值会与implicit parameter匹配并最终被传到Array.apply中。
3.  Array.apply方法，即上面的copiedArrayApply方法，会使用传进来的ClassTag[Int].newArray(...)来创建数组。通过这一系列步骤，程序员可以绕过java里不能通过范型直接创建数组的限制，比如T[] arr = new T[10]在java里不允许的。

当然除了解决范型数组创建的问题，ClassTag应该还有其他作用，并且ClassTag只是TypeTag的一个弱化形式，这里就先不展开讨论了。

那**[K : Ordering : ClassTag]**又是一个什么用法呢？通过把源代码解”糖“可以看到编译器其实是加了2个implicit parameter:
```scala
(implicit evidence$1: Ordering[K], implicit evidence$2:scala.reflect.ClassTag[K])
```

因为RangePartitioner里有这样的调用，需要传一个implicit的Ordering[K]过来。这个传过来的值应该就是evidence$1。
```scala
private var ordering = implicitly[Ordering[K]]
```

那evidence$1又是谁传进来的呢？其实还是编译器。编译器会在创建RangePartitioner的时候为部分K传一个预定义好的Ordering实例，如当K为Int时，提供math.this.Ordering.Int。
```scala
  trait IntOrdering extends Ordering[Int] {
    def compare(x: Int, y: Int) =
      if (x < y) -1
      else if (x == y) 0
      else 1
  }
  implicit object Int extends IntOrdering
```

所以**[K : Ordering : ClassTag]**其实是为方法签名提供了2个implicit parameter，并且在绝大多数情况下提供了预定义好的implicit parameter值。这样便可以绕过一部分类型擦除带来的问题。

总结：Scala为了绕过JVM的一些范型上的局限蛮拼的，也增大了代码的阅读难度。Implicit这个特性很灵活，很强大，也非常容易使人困惑。
