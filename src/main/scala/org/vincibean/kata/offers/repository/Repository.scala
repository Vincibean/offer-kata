package org.vincibean.kata.offers.repository

import scala.language.higherKinds

trait Repository[F[_], A, Id] {

  def insertOrUpdate(p: A): F[Int]

  def findAll: F[Seq[A]]

  def findBy(id: Id): F[Option[A]]

}
