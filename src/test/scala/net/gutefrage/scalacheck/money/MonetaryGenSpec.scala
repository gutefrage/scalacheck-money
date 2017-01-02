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
import javax.money.{Monetary, MonetaryAmount}

import org.javamoney.moneta.function.MonetaryQueries
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{MustMatchers, PropSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.collection.JavaConverters._

class MonetaryGenSpec
    extends PropSpec
    with MustMatchers
    with GeneratorDrivenPropertyChecks {
  property("∀currency. currency ∈ Monetary.getCurrencies") {
    // Just iterate over all currencies
    forAll(MonetaryGen.currency) { currency =>
      Monetary.getCurrencies() must contain(currency)
    }
  }

  property(
    "∀locale. currencyInLocale(locale) ∈ Monetary.getCurrencies(locale)") {
    val localeAndCurrency = for {
      locale <- Gen.oneOf(Locale.CANADA,
                          Locale.FRANCE,
                          Locale.GERMANY,
                          Locale.ITALY,
                          Locale.JAPAN,
                          Locale.CHINA,
                          Locale.UK,
                          Locale.US)
      currency <- MonetaryGen.currencyInLocale(locale)
    } yield (locale, currency)

    forAll(localeAndCurrency) {
      case (locale, currency) =>
        Monetary.getCurrencies(locale).asScala must contain(currency)
    }
  }

  property("∀currency. monetaryAmount(currency).getCurrency == currency") {
    val currencyAndAmount = for {
      currency <- MonetaryGen.currency
      amount <- MonetaryGen.monetaryAmount(currency)
    } yield (currency, amount)

    forAll(currencyAndAmount) {
      case (currency, amount) =>
        amount.getCurrency mustBe currency
    }
  }

  property(
    "∀min, max, currency. monetaryAmount(min, max, currency).getCurrency == currency") {
    val currencyAndAmount = for {
      d1 <- Arbitrary.arbitrary[Double]
      d2 <- Arbitrary.arbitrary[Double]
      currency <- MonetaryGen.currency
      amount <- MonetaryGen
        .chooseMonetaryAmount(Math.min(d1, d2), Math.max(d1, d2), currency)
    } yield (currency, amount)

    forAll(currencyAndAmount) {
      case (currency, amount) =>
        amount.getCurrency mustBe currency
    }
  }

  property("∀min, max, currency. min <= monetaryAmount(min, max, currency)") {
    val minAndAmount = for {
      d1 <- Arbitrary.arbitrary[Double]
      d2 <- Arbitrary.arbitrary[Double]
      min = Math.min(d1, d2)
      currency <- MonetaryGen.currency
      amount <- MonetaryGen
        .chooseMonetaryAmount(min, Math.max(d1, d2), currency)
    } yield (min, amount)

    forAll(minAndAmount) {
      case (min, amount) =>
        min must be <= amount.getNumber.doubleValueExact()
    }
  }

  property("∀min, max, currency. monetaryAmount(min, max, currency) <= max") {
    val maxAndAmount = for {
      d1 <- Arbitrary.arbitrary[Double]
      d2 <- Arbitrary.arbitrary[Double]
      max = Math.max(d1, d2)
      currency <- MonetaryGen.currency
      amount <- MonetaryGen
        .chooseMonetaryAmount(Math.min(d1, d2), max, currency)
    } yield (max, amount)

    forAll(maxAndAmount) {
      case (max, amount) =>
        amount.getNumber.doubleValueExact() must be <= max
    }
  }

  property("∀currency. posMonetaryAmount(currency).value >= 1") {
    forAll(MonetaryGen.posMonetaryAmount(MonetaryGen.currency)) { amount =>
      amount.getNumber.doubleValueExact must be >= 0.0
    }
  }

  property("∀currency. posMonetaryAmount(currency).value <= size") {
    val sizeAndAmount = for {
      size <- Gen.posNum[Int]
      amount <- Gen.resize(size,
                           MonetaryGen.posMonetaryAmount(MonetaryGen.currency))
    } yield (size, amount)

    forAll(sizeAndAmount) {
      case (size, amount) =>
        amount.getNumber.doubleValueExact must be <= size.toDouble
    }
  }
}
