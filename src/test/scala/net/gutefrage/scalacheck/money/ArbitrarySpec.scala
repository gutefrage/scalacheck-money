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

import javax.money.{CurrencyUnit, Monetary, MonetaryAmount}

import org.scalacheck.Arbitrary
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{MustMatchers, PropSpec}

class ArbitrarySpec
    extends PropSpec
    with MustMatchers
    with GeneratorDrivenPropertyChecks {

  import net.gutefrage.scalacheck.money.arbitrary._

  property("arbitrary currency") {
    import currency.any
    forAll { amount: MonetaryAmount =>
      Monetary.getCurrencies() must contain(amount.getCurrency)
    }
  }

  private def arbitraryAmountsMustHaveCode(code: String)(
      implicit a: Arbitrary[CurrencyUnit]) =
    forAll { amount: MonetaryAmount =>
      amount.getCurrency.getCurrencyCode mustBe code
    }

  property("byCode.CNY => ∀a. a.currency == CNY") {
    import currency.byCode.CNY
    arbitraryAmountsMustHaveCode("CNY")
  }
  property("byCode.EUR => ∀a. a.currency == EUR") {
    import currency.byCode.EUR
    arbitraryAmountsMustHaveCode("EUR")
  }
  property("byCode.GBP => ∀a. a.currency == GBP") {
    import currency.byCode.GBP
    arbitraryAmountsMustHaveCode("GBP")
  }
  property("byCode.JPY => ∀a. a.currency == JPY") {
    import currency.byCode.JPY
    arbitraryAmountsMustHaveCode("JPY")
  }
  property("byCode.USD => ∀a. a.currency == USD") {
    import currency.byCode.USD
    arbitraryAmountsMustHaveCode("USD")
  }
}
