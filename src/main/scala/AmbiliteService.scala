package ambilite.service

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

import scalalirc._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class AmbiLiteServiceActor extends Actor with AmbiLiteService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait AmbiLiteService extends HttpService {
  val lirc = ScalaLirc()

  val myRoute = {
    path("ambilite") {
      get {
        parameters("command") { command =>
          complete {
            processCommand(command)
          }
        }
      }
    }
  }

  def processCommand(command: String): String = {
    if (lirc.isDefined) {
      lirc.get.sendOnce("AmbiLite", command)
      s"Running command $command"
    } else {
      s"Error instantiating LIRC object"
    }
  }
}
