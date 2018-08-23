package org.vincibean.kata.offers

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.Json
import org.vincibean.kata.offers.domain.Offer
import org.vincibean.kata.offers.service.OfferService

import io.circe.generic.auto._
import io.circe.syntax._
import org.vincibean.kata.offers.implicits.SerDes._

object Routes extends ErrorAccumulatingCirceSupport {

  val invalidOffer: String = "This offer isn't valid anymore"
  val noOffer: String = "No offer with this ID"

  def routes(service: OfferService): Route =
    pathPrefix("offers") {
      get {
        pathEnd {
          onSuccess(service.all) { offers =>
            complete(offers.toVector.filter(o => service.isValid(o)))
          }
        } ~
          path(JavaUUID) { id =>
            onSuccess(service.get(id)) { maybeOffer =>
              val res: (StatusCode, Json) = maybeOffer
                .map {
                  case o if service.isInvalid(o) =>
                    (StatusCodes.Gone, invalidOffer.asJson)
                  case o => (StatusCodes.OK, o.asJson)
                }
                .getOrElse((StatusCodes.NotFound, noOffer.asJson))
              complete(res)
            }
          }
      } ~ post {
        pathEnd {
          entity(as[Offer]) { order =>
            onSuccess(service.create(order)) { result =>
              complete((StatusCodes.Created, result))
            }
          }
        }
      }
    }

}
