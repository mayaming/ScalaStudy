/**
 * Created by yaming.ma on 2015/3/11.
 */
import akka.actor.{Actor, ActorSystem, Props}
import spray.routing._
import spray.http._
import MediaTypes._
import akka.util.Timeout
import scala.concurrent.duration._
import akka.io.IO
import spray.can.Http
import akka.pattern.ask

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }
}

object SprayRestServer {
  def main(args: Array[String]) : Unit = {
    // we need an ActorSystem to host our application in
    implicit val system = ActorSystem("SprayRestServer")

    // create and start our service actor
    val service = system.actorOf(Props[MyServiceActor], "demo-service")

    implicit val timeout = Timeout(5.seconds)
    // start a new HTTP server on port 8080 with our service actor as the handler
    IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
  }
}
