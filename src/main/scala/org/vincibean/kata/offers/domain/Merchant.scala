package org.vincibean.kata.offers.domain

import java.util.UUID

import eu.timepit.refined.types.string.NonEmptyString

final case class Merchant(id: UUID, name: NonEmptyString)
