package org.vincibean.kata.offers.repository

import java.util.UUID

import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.execute.Result
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure
import org.vincibean.kata.offers.domain.Merchant
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

object MerchantRepositorySpec {
  def mockMerchant(name: String = "a mock merchant") = Merchant(UUID.randomUUID(), name)
}

class MerchantRepositorySpec(implicit ee: ExecutionEnv) extends Specification {
  override def is: SpecStructure =
    sequential ^ s2"""
         The merchant repository should
           return 1 when a new merchant is saved $s1
           return a merchant when its ID is given $s2
           return nothing when a non-existent ID is given $s3
           return all merchants $s4
           update a merchant when a merchant with the same UUID is saved $s5
      """

  implicit val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("storage-config")
  private val repo = new MerchantRepository()

  def s1: MatchResult[Future[Int]] = {
    repo.insertOrUpdate(MerchantRepositorySpec.mockMerchant("s1")) should beEqualTo(1).await
  }

  def s2: Result = {
    val mock = MerchantRepositorySpec.mockMerchant("s2")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      x <- repo.findBy(mock.id)
    } yield x
    res.map(_ must beSome(mock)).await
  }

  def s3: Result = repo.findBy(UUID.fromString(UUID.randomUUID().toString)).map(_ must beNone).await

  def s4: Result = {
    val mock = MerchantRepositorySpec.mockMerchant("s3")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      x <- repo.findAll
    } yield x
    res.map(_ must not beEmpty).await
  }

  def s5: Result = {
    val mock = MerchantRepositorySpec.mockMerchant("s4")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      _ <- repo.insertOrUpdate(mock.copy(name = "updated merchant"))
      x <- repo.findBy(mock.id)
    } yield x
    res.map(_ must beSome.which(_.name == "updated merchant")).await
  }

}
