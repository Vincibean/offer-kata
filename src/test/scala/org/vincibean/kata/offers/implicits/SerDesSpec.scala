package org.vincibean.kata.offers.implicits

import java.time.LocalDate

import io.circe.Decoder.Result
import org.joda.money.{BigMoney, CurrencyUnit}
import org.specs2.matcher.MatchResult
import org.specs2.scalacheck.ScalaCheckFunction1
import org.specs2.specification.core.SpecStructure
import org.specs2.{ScalaCheck, Specification}
import org.vincibean.kata.offers.Mocks._
import SerDes.{
  bigMoneyDecoder,
  bigMoneyEncoder,
  localDateDecoder,
  localDateEncoder
}
import io.circe.{HCursor, Json}

class SerDesSpec extends Specification with ScalaCheck {
  override def is: SpecStructure =
    s2"""
        Encoding and then decoding a BigMoney instance should return that instance $p1
        Encoding and then decoding a LocalDate instance should return that instance $p2
        Decoding a BigMoney instance containing an unknown currency should return that instance with USD as currency $p3
      """

  def p1: ScalaCheckFunction1[BigMoney, MatchResult[Result[BigMoney]]] = prop {
    bigMoney: BigMoney =>
      bigMoneyDecoder(bigMoneyEncoder(bigMoney).hcursor) must beRight(bigMoney)
  }

  def p2: ScalaCheckFunction1[LocalDate, MatchResult[Result[LocalDate]]] =
    prop { localDate: LocalDate =>
      localDateDecoder(localDateEncoder(localDate).hcursor) must beRight(
        localDate)
    }

  def p3: ScalaCheckFunction1[BigMoney, MatchResult[Result[BigMoney]]] = prop {
    bigMoney: BigMoney =>
      bigMoneyDecoder(
        HCursor.fromJson(
          Json.obj(
            ("currency", Json.fromString("I should not exist")),
            ("amount", Json.fromBigDecimal(bigMoney.getAmount))
          ))) must beRight.which(
        _.getCurrencyUnit must beEqualTo(CurrencyUnit.USD))
  }

}
