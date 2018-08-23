package org.vincibean.kata.offers

import java.time.{LocalDate, ZoneId}
import java.util.UUID

import org.joda.money.{BigMoney, CurrencyUnit}
import org.scalacheck.{Arbitrary, Gen}
import org.vincibean.kata.offers.domain.{Merchant, Offer, Product}

object Mocks {

  implicit val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary(
    Arbitrary.arbDate.arbitrary
      .map(_.toInstant.atZone(ZoneId.systemDefault()).toLocalDate))

  implicit val arbitraryBigMoney: Arbitrary[BigMoney] = Arbitrary {
    for {
      currency <- Gen.oneOf(
        Seq(CurrencyUnit.AUD,
            CurrencyUnit.CAD,
            CurrencyUnit.CHF,
            CurrencyUnit.EUR,
            CurrencyUnit.GBP,
            CurrencyUnit.JPY,
            CurrencyUnit.USD))
      amount <- Arbitrary.arbBigDecimal.arbitrary
    } yield BigMoney.of(currency, amount.bigDecimal)
  }

  implicit val arbitraryProduct: Arbitrary[Product] = Arbitrary {
    for {
      id <- Gen.uuid
      name <- Gen.alphaNumStr
      description <- Gen.alphaNumStr
    } yield Product(id, name, description)
  }

  implicit val arbitraryMerchant: Arbitrary[Merchant] = Arbitrary {
    for {
      id <- Gen.uuid
      description <- Gen.alphaNumStr
    } yield Merchant(id, description)
  }

  implicit val arbitraryOffer: Arbitrary[Offer] = Arbitrary {
    for {
      validTill <- Arbitrary.arbitrary[LocalDate]
      id <- Gen.uuid
      product <- Arbitrary.arbitrary[Product]
      merchant <- Arbitrary.arbitrary[Merchant]
      description <- Gen.alphaNumStr
      money <- Arbitrary.arbitrary[BigMoney]
    } yield Offer(id, product, merchant, description, money, validTill)
  }

  def mockOffer() =
    Offer(
      UUID.randomUUID(),
      Product(UUID.randomUUID(), "mockProduct", "a mock product"),
      Merchant(UUID.randomUUID(), "a mock merchant"),
      "a mock offer",
      BigMoney.of(CurrencyUnit.USD, BigDecimal(42L).bigDecimal),
      LocalDate.now().plusDays(2)
    )

}
