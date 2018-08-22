package org.vincibean.kata.offers.repository

import java.time.LocalDate
import java.util.UUID

import org.joda.money.{BigMoney, CurrencyUnit}
import org.specs2.Specification
import org.specs2.concurrent.ExecutionEnv
import org.specs2.execute.Result
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure
import org.vincibean.kata.offers.domain.Offer

import scala.concurrent.Future

class OfferRepositorySpec(implicit ee: ExecutionEnv) extends Specification {
  override def is: SpecStructure =
    sequential ^ s2"""
         The offer repository should
           return 1 when a new offer is saved $s1
           return an offer when its ID is given $s2
           return nothing when a non-existent ID is given $s3
           return all offers $s4
           update an offer when an offer with the same UUID is saved $s5
      """

  private val repo = new OfferRepository(new ProductRepository(), new MerchantRepository())

  def s1: MatchResult[Future[Int]] = {
    repo.insertOrUpdate(mockOffer("s1")) should beEqualTo(1).await
  }

  def s2: Result = {
    val mock = mockOffer("s2")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      x <- repo.findBy(mock.id)
    } yield x
    res.map(_ must beSome(mock)).await
  }

  def s3: Result = repo.findBy(UUID.fromString(UUID.randomUUID().toString)).map(_ must beNone).await

  def s4: Result = {
    val mock = mockOffer("s3")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      x <- repo.findAll
    } yield x
    res.map(_ must not beEmpty).await
  }

  def s5: Result = {
    val mock = mockOffer("s4")
    val res = for {
      _ <- repo.insertOrUpdate(mock)
      _ <- repo.insertOrUpdate(mock.copy(description = "updated product"))
      x <- repo.findBy(mock.id)
    } yield x
    res.map(_ must beSome.which(_.description == "updated product")).await
  }

  private def mockOffer(descr: String = "a mock product") = Offer(UUID.randomUUID(), ProductRepositorySpec.mockProduct(), MerchantRepositorySpec.mockMerchant(), descr, BigMoney.of(CurrencyUnit.USD, BigDecimal(42L).bigDecimal), LocalDate.now().plusDays(2))

}
