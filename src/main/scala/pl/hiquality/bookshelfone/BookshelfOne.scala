package pl.hiquality.bookshelfone

import cats.effect.{ContextShift, IO, Timer}
import tapir._
import io.circe.generic.auto._
import tapir.json.circe._
import tapir.server.http4s._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import cats.implicits._
import tapir.docs.openapi._
import tapir.openapi.circe.yaml._

import scala.concurrent.ExecutionContext

object BookshelfOne extends App {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ec)
  implicit val timer: Timer[IO] = IO.timer(ec)

  case class Error(code: Int, message: String)
  case class Book(title: String, authors: List[String])
  case class Books(books: List[Book])

  var storage = List(Book("Pan Tadeusz", List("Adam Mickiewicz")))

  val booksEndpoint = endpoint.get.in("book").out(jsonBody[Books])
  val booksLogic = (_: Unit) => IO(Books(storage).asRight[Unit])
  val booksService = booksEndpoint.toRoutes(booksLogic)

  val documentation = List(booksEndpoint).toOpenAPI("The Bookshelf One API", "0.1").toYaml

  val server = BlazeServerBuilder[IO]
    .bindHttp(8080, "localhost")
    .withHttpApp(Router("/" -> booksService,
                        "/docs" -> new SwaggerUI[IO](documentation).routes)
                 .orNotFound)
    .serve.compile.drain

  server.unsafeRunSync()
}
