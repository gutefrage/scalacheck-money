/*
 * Copyright 2016-2017 gutefrage.net GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gutefrage.scalacheck.money

import java.util.Locale
import javax.money.{
  CurrencyUnit,
  Monetary,
  MonetaryAmount,
  MonetaryAmountFactory
}

import org.scalacheck.{Arbitrary, Gen}

import scala.collection.JavaConverters._

/**
  * Provides [[org.scalacheck.Gen]] instances for
  * [[https://jcp.org/en/jsr/detail?id=354 JSR 354]] monetary types from
  * `javax.money`.
  */
object MonetaryGen {

  /**
    * Generate an arbitrary [[javax.money.CurrencyUnit]].
    *
    * This [[org.scalacheck.Gen]] instance will really generate all currencies
    * supported by the underlying `javax.money` implementation.  This can
    * include quite obscure currencies which unique properties (e.g. no minor
    * unit).
    */
  def currency: Gen[CurrencyUnit] =
    Gen.oneOf(Monetary.getCurrencies().asScala.toSeq)

  /**
    * Generate an arbitrary [[javax.money.CurrencyUnit]] for a
    * `java.util.Locale`.
    *
    * This [[org.scalacheck.Gen]] instance only generates currencies used in the
    * given locale.  For instance, `java.util.Locale.US` would typically only
    * generate a [[javax.money.CurrencyUnit]] for `USD`.
    *
    * @param locale The locale whose currencies to use
    */
  def currencyInLocale(locale: Locale): Gen[CurrencyUnit] =
    Gen.oneOf(Monetary.getCurrencies(locale).asScala.toSeq)

  /**
    * Private helper to guide scala's type inference through the type mess of
    * `Monetary.getDefaultAmountFactory`.
    */
  private def defaultFactory: MonetaryAmountFactory[_ <: MonetaryAmount] =
    Monetary.getDefaultAmountFactory

  /**
    * Generate an arbitrary [[javax.money.MonetaryAmount]], using a given
    * [[org.scalacheck.Gen]] instance for the currency of the amount.
    *
    * @param currency A [[org.scalacheck.Gen]] for currency units to use
    * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currency]]
    * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currencyInLocale]]
    */
  def monetaryAmount(currency: Gen[CurrencyUnit]): Gen[MonetaryAmount] =
    for {
      factory <- currency.map(defaultFactory.setCurrency)
      number <- Arbitrary.arbitrary[Double]
    } yield factory.setNumber(number).create()

  /**
    * Generate an arbitrary [[javax.money.MonetaryAmount]] with given limits,
    * using a given [[org.scalacheck.Gen]] instance for the currency of the
    * amount.
    *
    * @param min The minimal value
    * @param max The maximal value
    * @param currency The currency
    * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currency]]
    * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currencyInLocale]]
    */
  def chooseMonetaryAmount(
      min: Double,
      max: Double,
      currency: Gen[CurrencyUnit]
  ): Gen[MonetaryAmount] =
    for {
      factory <- currency.map(defaultFactory.setCurrency)
      number <- Gen.chooseNum(min, max)
    } yield factory.setNumber(number).create(): MonetaryAmount

  /**
    * Generate an arbitrary '''positive''' [[javax.money.MonetaryAmount]] with
    * given limits, using a given [[org.scalacheck.Gen]] instance for the
    * currency of the amount.
    *
    * @param currency The currency
    * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currency]]
    * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currencyInLocale]]
    */
  def posMonetaryAmount(currency: Gen[CurrencyUnit]): Gen[MonetaryAmount] =
    Gen.sized(
      size =>
        // Make sure size is large enough
        chooseMonetaryAmount(1.0, Math.max(1, size.toDouble), currency))
}
