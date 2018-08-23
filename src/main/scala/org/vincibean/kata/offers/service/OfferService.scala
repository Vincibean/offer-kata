package org.vincibean.kata.offers.service

import java.util.UUID

import org.vincibean.kata.offers.domain.Offer
import org.vincibean.kata.offers.repository.Repository

import scala.concurrent.Future

trait OfferService {

  def create(o: Offer): Future[Int]

  def all: Future[Seq[Offer]]

  def get(id: UUID): Future[Option[Offer]]

}

class OfferServiceImpl(repo: Repository[Future, Offer, UUID])
    extends OfferService {

  override def create(o: Offer): Future[Int] = repo.insertOrUpdate(o)

  override def all: Future[Seq[Offer]] = repo.findAll

  override def get(id: UUID): Future[Option[Offer]] = repo.findBy(id)

}
