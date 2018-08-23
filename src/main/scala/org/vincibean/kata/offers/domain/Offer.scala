package org.vincibean.kata.offers.domain

import java.time.LocalDate
import java.util.UUID

import org.joda.money.BigMoney

final case class Offer(id: UUID,
                       product: Product,
                       merchant: Merchant,
                       description: String,
                       money: BigMoney,
                       validTill: LocalDate)
