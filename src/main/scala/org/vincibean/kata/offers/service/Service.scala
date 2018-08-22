package org.vincibean.kata.offers.service

import scala.language.higherKinds

trait Service[F[_], A, Id] {

  def create(o: A): F[Int]

  def all: F[Seq[A]]

  def get(id: Id): F[Option[A]]

}
