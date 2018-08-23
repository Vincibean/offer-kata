package org.vincibean.kata.offers.repository

import java.util.UUID

import org.vincibean.kata.offers.dao.Tables
import org.vincibean.kata.offers.domain.Merchant
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class MerchantRepository(implicit ec: ExecutionContext)
    extends Repository[Future, Merchant, UUID] {

  private val dbConfig: DatabaseConfig[JdbcProfile] =
    DatabaseConfig.forConfig[JdbcProfile]("storage-config")
  import dbConfig.profile.api._

  private val db = dbConfig.db

  private val merchants = Tables.Merchants

  override def insertOrUpdate(p: Merchant): Future[Int] =
    db.run(merchants.insertOrUpdate(asRow(p)))

  override def findAll: Future[Seq[Merchant]] =
    db.run(merchants.result).map(_.map(fromRow))

  override def findBy(id: UUID): Future[Option[Merchant]] =
    db.run(
      merchants
        .filter(_.id === id)
        .result
        .headOption
        .map(_.map(fromRow)))

  private def asRow(merchant: Merchant): Tables.MerchantsRow =
    Tables.MerchantsRow(merchant.id, merchant.name)

  private def fromRow(row: Tables.MerchantsRow): Merchant =
    Merchant(row.id, row.name)

}
