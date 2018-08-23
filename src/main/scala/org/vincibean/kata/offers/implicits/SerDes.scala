package org.vincibean.kata.offers.implicits

import java.time.LocalDate

import io.circe.{Decoder, Encoder, HCursor, Json}
import org.joda.money.{CurrencyUnit, BigMoney}

import scala.util.Try

object SerDes {

  implicit val bigMoneyEncoder: Encoder[BigMoney] = (a: BigMoney) =>
    Json.obj(
      ("currency", Json.fromString(a.getCurrencyUnit.getCode)),
      ("amount", Json.fromBigDecimal(a.getAmount))
  )

  implicit val bigMoneyDecoder: Decoder[BigMoney] = (c: HCursor) =>
    for {
      currency <- c.downField("currency").as[String]
      amount <- c.downField("amount").as[BigDecimal]
    } yield
      BigMoney.of(Try(CurrencyUnit.of(currency)).getOrElse(CurrencyUnit.USD),
                  amount.bigDecimal)

  implicit val localDateEncoder: Encoder[LocalDate] = (a: LocalDate) =>
    Json.obj(
      ("year", Json.fromInt(a.getYear)),
      ("month", Json.fromBigDecimal(a.getMonthValue)),
      ("dayOfMonth", Json.fromBigDecimal(a.getDayOfMonth))
  )

  implicit val localDateDecoder: Decoder[LocalDate] = (c: HCursor) =>
    for {
      year <- c.downField("year").as[Int]
      month <- c.downField("month").as[Int]
      dayOfMonth <- c.downField("dayOfMonth").as[Int]
    } yield {
      LocalDate.of(year, month, dayOfMonth)
  }

}
