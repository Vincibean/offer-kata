package org.vincibean.kata.offers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.vincibean.kata.offers.repository.{
  MerchantRepository,
  OfferRepository,
  ProductRepository
}
import org.vincibean.kata.offers.service.OfferService
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer extends App {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val dbConfig: DatabaseConfig[JdbcProfile] =
    DatabaseConfig.forConfig[JdbcProfile]("storage-config")

  val bindingFuture = Http().bindAndHandle(
    Routes.routes(OfferService(
      new OfferRepository(new ProductRepository(), new MerchantRepository()))),
    "localhost",
    8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
