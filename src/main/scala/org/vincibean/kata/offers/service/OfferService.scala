package org.vincibean.kata.offers.service

import java.time.LocalDate
import java.util.UUID

import org.vincibean.kata.offers.domain.Offer
import org.vincibean.kata.offers.repository.Repository

import scala.concurrent.Future

trait OfferService {

  def create(o: Offer): Future[Int]

  def all: Future[Seq[Offer]]

  def get(id: UUID): Future[Option[Offer]]

  def isValid(o: Offer, now: LocalDate = LocalDate.now()): Boolean =
    now.isBefore(o.validTill)

  def isInvalid(o: Offer, now: LocalDate = LocalDate.now()): Boolean =
    !isValid(o, now)

}

object OfferService {
  def apply(repo: Repository[Future, Offer, UUID]): OfferService =
    new OfferService {

      override def create(o: Offer): Future[Int] = repo.insertOrUpdate(o)

      override def all: Future[Seq[Offer]] = repo.findAll

      override def get(id: UUID): Future[Option[Offer]] = repo.findBy(id)

    }
}
