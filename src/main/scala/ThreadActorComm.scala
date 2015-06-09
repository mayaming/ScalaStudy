import akka.actor.Actor.Receive
import akka.actor._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.Success

/**
 * Created by root on 5/14/15.
 */

class SlaveActor extends Actor {
  override def receive: Actor.Receive = {
    case (p: ActorPath, "IndirectRequest") => {
      sender ! (p, "IndirectResponse")
    }
  }
}

class MyActor extends Actor {
  override def receive: Receive = {
    case "DirectRequest" => sender ! "DirectResponse"
    case "IndirectRequest" => {
      context.actorOf(Props(classOf[SlaveActor])) ! (sender.path, "IndirectRequest")
    }
    case (p: ActorPath, s:String) => {
      println("Before sending indirect response back to " + p.toString)
      val as = context.actorSelection(p)
      println("Actor selection: " + as.toString())
      as ! (p, s)
    }
  }
}

object ThreadActorComm {
  def main(args: Array[String]):Unit = {
    val system = ActorSystem("system")
    val actor = system.actorOf(Props(classOf[MyActor]))
    implicit val timeout = Timeout(5 second)

    val t = new Thread() {
      override def run() = {
        try { println(Await.result(actor ? "DirectRequest", timeout.duration)) }
        catch { case e:Throwable => e.printStackTrace() }
        println("Direct communication test passed")

        try { println(Await.result(actor ? "IndirectRequest", timeout.duration)) }
        catch { case e:Throwable => e.printStackTrace() }
        println("Indirect communication test passed")
      }
    }

    t.start()
    t.join(0)
  }
}
