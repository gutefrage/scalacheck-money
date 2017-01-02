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
  * Arbitraries for monetary types
  */
object arbitrary {

  /**
    * Arbitrary instance for monetary amounts.
    *
    * Needs additional arbitraries from the [[currency]] object to select the
    * currency.
    *
    * @param currency The currency to use.
    */
  implicit def arbitraryMonetaryAmount(
      implicit currency: Arbitrary[CurrencyUnit]): Arbitrary[MonetaryAmount] =
    Arbitrary(MonetaryGen.monetaryAmount(currency.arbitrary))

  /**
    * Provides arbitrary instances for currencies.
    *
    * We recommend to locally import the instance that you need.
    */
  object currency {

    /**
      * Generates any currency.
      */
    implicit val any: Arbitrary[CurrencyUnit] = Arbitrary(MonetaryGen.currency)

    object byCode {
      private def arbitraryForCode(code: String): Arbitrary[CurrencyUnit] =
        Arbitrary(Gen.const(Monetary.getCurrency(code)))

      // Only specific currencies
      implicit def CNY: Arbitrary[CurrencyUnit] =
        arbitraryForCode("CNY")
      implicit def EUR: Arbitrary[CurrencyUnit] =
        arbitraryForCode("EUR")
      implicit def GBP: Arbitrary[CurrencyUnit] =
        arbitraryForCode("GBP")
      implicit def JPY: Arbitrary[CurrencyUnit] =
        arbitraryForCode("JPY")
      implicit def USD: Arbitrary[CurrencyUnit] =
        arbitraryForCode("USD")
    }
  }
}
