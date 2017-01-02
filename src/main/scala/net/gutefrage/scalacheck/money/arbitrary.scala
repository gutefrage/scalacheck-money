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

import javax.money.{CurrencyUnit, Monetary, MonetaryAmount}

import org.scalacheck.{Arbitrary, Gen}

/**
  * [[org.scalacheck.Arbitrary$ Arbitrary]] instances for
  * [[https://jcp.org/en/jsr/detail?id=354 JSR 354]] monetary types from
  * `javax.money`.
  *
  * Import as wildcard to bring all instances in scope:
  *
  * {{{
  * import net.gutefrage.money.arbitrary._
  * }}}
  *
  * To use arbitrary monetary amounts you also need an instance for a currency
  * in scope:
  *
  * {{{
  * import javax.money.MonetaryAmount
  * import net.gutefrage.scalacheck.money.arbitrary._
  *
  * property("amounts in any currency") {
  *   import currency.any
  *   forAll { amount: MonetaryAmount =>
  *     …
  *   }
  * }
  *
  * property("amounts in €") {
  *   import currency.byCode.EUR
  *   forAll { amount: MonetaryAmount =>
  *     …
  *   }
  * }
  *
  * }}}
  */
object arbitrary {

  /**
    * [[org.scalacheck.Arbitrary$ Arbitrary]] instance for
    * `javax.money.MonetaryAmount`.
    *
    * This instance requires an [[org.scalacheck.Arbitrary$ Arbitrary]] instance
    * for [[javax.money.CurrencyUnit]] in implicit scope.  The package
    * [[net.gutefrage.scalacheck.money.arbitrary.currency]] provides a couple of
    * these instances.
    *
    * @param currency An [[org.scalacheck.Arbitrary$ Arbitrary]] instance for
    *                 the currency to use
    */
  implicit def arbitraryMonetaryAmount(
      implicit currency: Arbitrary[CurrencyUnit]
  ): Arbitrary[MonetaryAmount] =
    Arbitrary(MonetaryGen.monetaryAmount(currency.arbitrary))

  /**
    * Provides different [[org.scalacheck.Arbitrary$ Arbitrary]] instances for
    * [[javax.money.CurrencyUnit]].
    *
    * It's recommended to import the required instances in local scope!
    */
  object currency {

    /**
      * [[org.scalacheck.Arbitrary$ Arbitrary]] instance that generates all
      * currencies.
      *
      * @see [[net.gutefrage.scalacheck.money.MonetaryGen.currency]]
      */
    implicit val any: Arbitrary[CurrencyUnit] = Arbitrary(MonetaryGen.currency)

    /**
      * [[org.scalacheck.Arbitrary$ Arbitrary]] instances that only generate
      * currencies with a particular currency code.
      */
    object byCode {
      private def arbitraryForCode(code: String): Arbitrary[CurrencyUnit] =
        Arbitrary(Gen.const(Monetary.getCurrency(code)))

      // Only specific currencies
      /**
        * Chinese yuan (CNY).
        */
      implicit def CNY: Arbitrary[CurrencyUnit] =
        arbitraryForCode("CNY")

      /**
        * Euro (EUR)
        */
      implicit def EUR: Arbitrary[CurrencyUnit] =
        arbitraryForCode("EUR")

      /**
        * British pound (GBP)
        */
      implicit def GBP: Arbitrary[CurrencyUnit] =
        arbitraryForCode("GBP")

      /**
        * Japanese yen (JPY)
        */
      implicit def JPY: Arbitrary[CurrencyUnit] =
        arbitraryForCode("JPY")

      /**
        * US dollar (USD)
        */
      implicit def USD: Arbitrary[CurrencyUnit] =
        arbitraryForCode("USD")
    }
  }
}
