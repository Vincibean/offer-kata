package org.vincibean.kata.offers

import java.time.LocalDate
import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.Specs2RouteTest
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import org.specs2.mutable.Specification
import org.vincibean.kata.offers.domain.Offer
import org.vincibean.kata.offers.service.{OfferService, OfferServiceSpec}
import io.circe.generic.auto._
import io.circe.syntax._
import org.vincibean.kata.offers.implicits.SerDes._

import scala.concurrent.Future

class RoutesSpec
    extends Specification
    with ErrorAccumulatingCirceSupport
    with Specs2RouteTest {

  private val mockOffer = OfferServiceSpec.mockOffer("RoutesSpec")
  private val invalidOffer =
    mockOffer.copy(validTill = LocalDate.now().minusDays(2))

  private def fullMockService(offer: Offer): OfferService =
    new OfferService {

      override def create(o: Offer): Future[Int] = Future.successful(1)

      override def all: Future[Seq[Offer]] = Future.successful(Seq(offer))

      override def get(id: UUID): Future[Option[Offer]] =
        Future.successful(Option(offer).filter(_.id == id))
    }

  private val emptyMockService: OfferService =
    new OfferService {
      override def create(o: Offer): Future[Int] = Future.successful(0)

      override def all: Future[Seq[Offer]] = Future.successful(Nil)

      override def get(id: UUID): Future[Option[Offer]] =
        Future.successful(None)
    }

  private val fullRoute = Routes.routes(fullMockService(mockOffer))
  private val invalidRoute = Routes.routes(fullMockService(invalidOffer))
  private val emptyRoute = Routes.routes(emptyMockService)
  private val notExistingID = "2c8e886a-1281-4daf-a83b-317956a50a91"

  "The service" should {

    "return all offers for GET requests to the /offers path" in {
      Get("/offers") ~> fullRoute ~> check {
        responseAs[List[Offer]] should contain(mockOffer)
      }
    }

    "return no offer for GET requests to the /offers path when they are invalid" in {
      Get("/offers") ~> invalidRoute ~> check {
        responseAs[List[Offer]] should beEmpty
      }
    }

    "return no offer for GET requests to the /offers path when no offer was saved" in {
      Get("/offers") ~> emptyRoute ~> check {
        responseAs[List[Offer]] should beEmpty
      }
    }

    "return a specific offer for GET requests to the specific UUID" in {
      Get(s"/offers/${mockOffer.id}") ~> fullRoute ~> check {
        responseAs[Offer] should beEqualTo(mockOffer)
      }
    }

    "return no offer for GET requests when an offer associated to a valid UUID can't be found" in {
      Get(s"/offers/${mockOffer.id}") ~> emptyRoute ~> check {
        (status shouldEqual StatusCodes.NotFound) and (responseAs[String] shouldEqual Routes.noOffer)
      }
    }

    "return no offer for GET requests when an invalid UUID is given" in {
      Get(s"/offers/$notExistingID") ~> fullRoute ~> check {
        (status shouldEqual StatusCodes.NotFound) and (responseAs[String] shouldEqual Routes.noOffer)
      }
    }

    "return an error message for GET requests to a no-more-valid offer" in {
      Get(s"/offers/${invalidOffer.id}") ~> invalidRoute ~> check {
        (status shouldEqual StatusCodes.Gone) and (responseAs[String] shouldEqual Routes.invalidOffer)
      }
    }

    "create a new offer for POST requests to the /offers path" in {
      Post("/offers", mockOffer.asJson) ~> fullRoute ~> check {
        responseAs[Int] shouldEqual 1
      }
    }
  }

}
