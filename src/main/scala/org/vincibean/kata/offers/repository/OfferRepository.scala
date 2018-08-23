package org.vincibean.kata.offers.repository

import java.sql.Date
import java.util.UUID

import cats._
import cats.implicits._
import org.joda.money.{BigMoney, CurrencyUnit}
import org.vincibean.kata.offers.dao.Tables
import org.vincibean.kata.offers.domain.{
  Merchant,
  Offer,
  Product => OfferedProduct
}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class OfferRepository(
    productRepository: Repository[Future, OfferedProduct, UUID],
    merchantRepository: Repository[Future, Merchant, UUID])(
    implicit ec: ExecutionContext)
    extends Repository[Future, Offer, UUID] {

  private val dbConfig: DatabaseConfig[JdbcProfile] =
    DatabaseConfig.forConfig[JdbcProfile]("storage-config")
  import dbConfig.profile.api._

  private val db = dbConfig.db

  private val offers = Tables.Offers

  override def insertOrUpdate(p: Offer): Future[Int] =
    for {
      _ <- productRepository.insertOrUpdate(p.product)
      _ <- merchantRepository.insertOrUpdate(p.merchant)
      ins <- db.run(offers.insertOrUpdate(asRow(p)))
    } yield ins

  override def findAll: Future[Seq[Offer]] = {
    val x: Future[Seq[Tables.OffersRow]] = db.run(offers.result)
    x.flatMap(ss => Future.traverse(ss)(fromRow)).map(_.flatten)
  }

  override def findBy(id: UUID): Future[Option[Offer]] = {
    val x: Future[Option[Tables.OffersRow]] =
      db.run(offers.filter(_.id === id).result.headOption)
    x.flatMap(ss =>
      Future.sequence(ss.toVector.map(fromRow)).map(_.flatten.headOption))
  }

  private def fromRow(x: Tables.OffersRow): Future[Option[Offer]] = {
    val prod = productRepository.findBy(x.productId)
    val merch = merchantRepository.findBy(x.merchantId)
    compose(Future.successful(Option(x)), prod, merch)
  }

  private def asRow(offer: Offer): Tables.OffersRow =
    Tables.OffersRow(
      offer.id,
      offer.product.id,
      offer.merchant.id,
      offer.description,
      offer.money.getAmount,
      offer.money.getCurrencyUnit.getCurrencyCode,
      Date.valueOf(offer.validTill)
    )

  private def compose(row: Future[Option[Tables.OffersRow]],
                      prod: Future[Option[OfferedProduct]],
                      merch: Future[Option[Merchant]]): Future[Option[Offer]] =
    Applicative[Future]
      .compose[Option]
      .map3(row, prod, merch)(
        (r, p, m) =>
          Offer(r.id,
                p,
                m,
                r.description,
                BigMoney.of(CurrencyUnit.of(r.currencyId), r.price.bigDecimal),
                r.validity.toLocalDate))

}
