package org.vincibean.kata.offers.service

import java.time.LocalDate
import java.util.UUID

import org.scalacheck.Prop
import org.specs2.concurrent.ExecutionEnv
import org.specs2.execute.Result
import org.specs2.matcher.MatchResult
import org.specs2.scalacheck.ScalaCheckFunction2
import org.specs2.specification.core.SpecStructure
import org.specs2.{ScalaCheck, Specification}
import org.vincibean.kata.offers.Mocks
import org.vincibean.kata.offers.Mocks._
import org.vincibean.kata.offers.domain.Offer
import org.vincibean.kata.offers.repository.Repository

import scala.collection.mutable
import scala.concurrent.Future

class OfferServiceSpec(implicit ee: ExecutionEnv)
    extends Specification
    with ScalaCheck {
  override def is: SpecStructure =
    s2"""
        The offer service should
          return 1 when a new offer is saved $s1
          return an offer when its ID is given $s2
          return nothing when a non-existent ID is given $s3
          return no offer when the repository is empty $s4
          return all offers $s5
          update an offer when an offer with the same UUID is saved $s6
          mark an offer as valid if its validity is before the given date $p1
          mark an offer as invalid if its validity is after the given date $p2
          mark an offer as invalid if its validity is equal to the given date $p3
      """

  private def mockRepo(): Repository[Future, Offer, UUID] =
    new Repository[Future, Offer, UUID] {

      private[this] var rep = mutable.Map.empty[UUID, Offer]

      override def insertOrUpdate(p: Offer): Future[Int] = {
        rep += (p.id -> p)
        Future.successful(1)
      }

      override def findAll: Future[Seq[Offer]] =
        Future.successful(rep.values.toVector)

      override def findBy(id: UUID): Future[Option[Offer]] =
        Future.successful(rep.get(id))
    }

  private def mockService() = OfferService(mockRepo())

  def s1: MatchResult[Future[Int]] =
    mockService().create(Mocks.mockOffer()) should beEqualTo(1).await

  def s2: Result = {
    val mock = Mocks.mockOffer()
    val service = mockService()
    val res = for {
      _ <- service.create(mock)
      x <- service.get(mock.id)
    } yield x
    res.map(_ must beSome(mock)).await
  }

  def s3: Result = mockService().get(UUID.randomUUID()).map(_ must beNone).await

  def s4: Result = mockService().all.map(_ must beEmpty).await

  def s5: Result = {
    val mock = Mocks.mockOffer()
    val service = mockService()
    val res = for {
      _ <- service.create(mock)
      x <- service.all
    } yield x
    res.map(_ must not beEmpty).await
  }

  def s6: Result = {
    val mock = Mocks.mockOffer()
    val service = mockService()
    val res = for {
      _ <- service.create(mock)
      _ <- service.create(mock.copy(description = "updated product"))
      x <- service.get(mock.id)
    } yield x
    res.map(_ must beSome.which(_.description == "updated product")).await
  }

  def p1: ScalaCheckFunction2[LocalDate, Offer, Prop] = prop {
    (now: LocalDate, offer: Offer) =>
      offer.validTill.isAfter(now) ==>
        mockService().isValid(offer, now)
  }

  def p2: ScalaCheckFunction2[LocalDate, Offer, Prop] = prop {
    (now: LocalDate, offer: Offer) =>
      offer.validTill.isBefore(now) ==>
        mockService().isInvalid(offer, now)
  }

  def p3: ScalaCheckFunction2[LocalDate, Offer, Prop] = prop {
    (now: LocalDate, offer: Offer) =>
      mockService().isInvalid(offer.copy(validTill = now), now)
  }

}
