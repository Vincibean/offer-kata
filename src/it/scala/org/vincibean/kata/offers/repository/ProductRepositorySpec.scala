package org.vincibean.kata.offers.repository

import java.util.UUID

import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.execute.Result
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure
import org.vincibean.kata.offers.domain.Product
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

object ProductRepositorySpec {
  def mockProduct(descr: String = "a mock product") = Product(UUID.randomUUID(), "mockProduct", descr)
}

class ProductRepositorySpec(implicit ee: ExecutionEnv) extends Specification {
  override def is: SpecStructure =
    sequential ^ s2"""
         The product repository should
           return 1 when a new product is saved $s1
           return a product when its ID is given $s2
           return nothing when a non-existent ID is given $s3
           return all products $s4
           update a product when a product with the same UUID is saved $s5
      """

  implicit val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("storage-config")
  private val repo = new ProductRepository()

  def s1: MatchResult[Future[Int]] = {
    repo.insertOrUpdate(ProductRepositorySpec.mockProduct("s1")) should beEqualTo(1).await
  }

  def s2: Result = {
    val mock = ProductRepositorySpec.mockProduct("s2")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      x <- repo.findBy(mock.id)
    } yield x
    res.map(_ must beSome(mock)).await
  }

  def s3: Result = repo.findBy(UUID.fromString(UUID.randomUUID().toString)).map(_ must beNone).await

  def s4: Result = {
    val mock = ProductRepositorySpec.mockProduct("s3")
    val res: Future[Seq[Product]] = for {
      _ <- repo.insertOrUpdate(mock)
      x <- repo.findAll
    } yield x
    res.map(_ must not beEmpty).await
  }

  def s5: Result = {
    val mock = ProductRepositorySpec.mockProduct("s4")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      _ <- repo.insertOrUpdate(mock.copy(description = "updated product"))
      x <- repo.findBy(mock.id)
    } yield x
    res.map(_ must beSome.which(_.description == "updated product")).await
  }

}
