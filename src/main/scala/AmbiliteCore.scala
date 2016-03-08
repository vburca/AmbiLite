package ambilite.service

import io.finch._

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Await

import io.circe.{Encoder, Json}

import scalalirc._


object AmbiliteCore extends App {

  case class Input(cmd: String)
  case class Output(result: String) {
    implicit val encodeException: Encoder[Exception] = Encoder.instance(e =>
      Json.obj(
        "type" -> Json.string(e.getClass.getSimpleName),
        "message" -> Json.string(e.getMessage)
      )
    )
  }

  val command: Endpoint[Input] = body.as[Input]

  val postCommand: Endpoint[Output] = post("command" :: command) { input: Input =>
    Ok(Output(input.cmd))
     /*
    if (lirc.isDefined) {
      lirc.get.sendOnce("AmbiLite", command)
      s"Running command $command"
    } else {
      s"Error instantiating LIRC object"
    } */
  } handle {
    case e: Exception => BadRequest(e)
  }

  println("Spinning up the Ambilite API Server...")

  val api: Service[Request, Response] = postCommand.toService

  Await.ready(Http.server.serve(":8080", api))
}
