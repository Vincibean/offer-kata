package org.vincibean.kata.offers.domain

import java.time.LocalDate
import java.util.UUID

import eu.timepit.refined.types.string.NonEmptyString
import org.joda.money.BigMoney

final case class Offer(id: UUID,
                       product: Product,
                       merchant: Merchant,
                       description: NonEmptyString,
                       money: BigMoney,
                       validTill: LocalDate)
