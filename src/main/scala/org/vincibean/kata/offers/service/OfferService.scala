package org.vincibean.kata.offers.service

import java.util.UUID

import org.vincibean.kata.offers.domain.Offer
import org.vincibean.kata.offers.repository.Repository

import scala.concurrent.Future

class OfferService(repo: Repository[Future, Offer, UUID])
    extends Service[Future, Offer, UUID] {

  override def create(o: Offer): Future[Int] = repo.insertOrUpdate(o)

  override def all: Future[Seq[Offer]] = repo.findAll

  override def get(id: UUID): Future[Option[Offer]] = repo.findBy(id)

}
