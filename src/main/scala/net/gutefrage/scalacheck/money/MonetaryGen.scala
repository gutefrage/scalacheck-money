/*
 * Copyright 2016 gutefrage.net GmbH
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
  * Provides generators for monetary types.
  */
object MonetaryGen {

  /**
    * Generate an arbitrary currency.
    */
  def currency: Gen[CurrencyUnit] =
    Gen.oneOf(Monetary.getCurrencies().asScala.toSeq)

  /**
    * Generate an arbitrary currency for a locale.
    *
    * @param locale The locale whose currencies to use
    */
  def currencyInLocale(locale: Locale): Gen[CurrencyUnit] =
    Gen.oneOf(Monetary.getCurrencies(locale).asScala.toSeq)

  /**
    * Private helper to guide scala's type inference through the type mess of
    * Monetary.getDefaultAmountFactory
    */
  private def defaultFactory: MonetaryAmountFactory[_ <: MonetaryAmount] =
    Monetary.getDefaultAmountFactory

  /**
    * Generate an arbitrary monetary amount in a currency.
    *
    * @param currency The currencies for the amount
    */
  def monetaryAmount(currency: Gen[CurrencyUnit]): Gen[MonetaryAmount] =
    for {
      factory <- currency.map(defaultFactory.setCurrency)
      number <- Arbitrary.arbitrary[Double]
    } yield factory.setNumber(number).create()

  /**
    * Generate an arbitrary amount in a currency with given limits.
    *
    * @param min The minimal value
    * @param max The maximal value
    * @param currency The currency
    */
  def chooseMonetaryAmount(min: Double,
                           max: Double,
                           currency: Gen[CurrencyUnit]): Gen[MonetaryAmount] =
    for {
      factory <- currency.map(defaultFactory.setCurrency)
      number <- Gen.chooseNum(min, max)
    } yield factory.setNumber(number).create(): MonetaryAmount

  /**
    * Generate a positive monetary amount.
    *
    * @param currency The currency
    */
  def posMonetaryAmount(currency: Gen[CurrencyUnit]): Gen[MonetaryAmount] =
    Gen.sized(
      size =>
        // Make sure size is large enough
        chooseMonetaryAmount(1.0, Math.max(1, size.toDouble), currency))
}
