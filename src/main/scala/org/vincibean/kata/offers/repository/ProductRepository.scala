package org.vincibean.kata.offers.repository

import java.util.UUID

import org.vincibean.kata.offers.dao.Tables
import org.vincibean.kata.offers.domain.{Product => OfferedProduct}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ProductRepository(implicit dbConfig: DatabaseConfig[JdbcProfile],
                        ec: ExecutionContext)
    extends Repository[Future, OfferedProduct, UUID] {

  import dbConfig.profile.api._

  private val db = dbConfig.db

  private val products = Tables.Products

  def insertOrUpdate(p: OfferedProduct): Future[Int] =
    db.run(products.insertOrUpdate(asRow(p)))

  def findAll: Future[Seq[OfferedProduct]] =
    db.run(products.result).map(_.map(fromRow))

  def findBy(id: UUID): Future[Option[OfferedProduct]] =
    db.run(
      products
        .filter(_.id === id)
        .result
        .headOption
        .map(_.map(fromRow)))

  private def asRow(prod: OfferedProduct): Tables.ProductsRow =
    Tables.ProductsRow(prod.id, prod.name, prod.description)
  private def fromRow(row: Tables.ProductsRow): OfferedProduct =
    OfferedProduct(row.id, row.name, row.description)

}
