/**
 * Created by root on 12/10/14.
 */
import akka.actor.{Actor, ActorSystem, Props}

class EchoServer extends Actor {
  def receive = {
    case msg: String => println("echo " + msg)
  }
}

object ActorExample {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem()
    val echoServer = system.actorOf(Props[EchoServer])
    echoServer ! "hello, world"
    system.shutdown()
  }
}
