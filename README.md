# scalacheck-money

[![Apache License 2.0](https://img.shields.io/github/license/gutefrage/scalacheck-money.svg)](https://github.com/gutefrage/scalacheck-money/blob/master/LICENSE)
[![Latest version](https://index.scala-lang.org/gutefrage/scalacheck-money/scalacheck-money/latest.svg)](https://index.scala-lang.org/gutefrage/scalacheck-money/scalacheck-money)
[![Travis master build status](https://img.shields.io/travis/gutefrage/scalacheck-money/master.svg)](https://travis-ci.org/gutefrage/scalacheck-money)

scalacheck arbitraries and generators for [JSR 354][] monetary types, e.g.
`javax.money`.

[JSR 354]: https://jcp.org/en/jsr/detail?id=354

## Installation

```scala
libraryDependencies += "net.gutefrage" %% "scalacheck-money" % "0.2"
```

Until [JSR 354][] is included in Java you will also need an implementation, e.g.

```scala
libraryDependencies += "org.javamoney" % "moneta" % "1.1"
```

but if you're using this library you probably have one already.

## Usage

The main package `net.gutefrage.scalacheck.money` provides the following API:

* `MonetaryGen` provides various `org.scalacheck.Gen` instances for monetary
  types like `MonetaryAmount` or `CurrencyUnit`.
* `arbitrary` provides general arbitrary instances for those types.

Arbitrary monetary amounts require an arbitrary currency.  This library provides
implicits for any currency as well for some popular currencies:

```scala
import javax.money.MonetaryAmount
import net.gutefrage.scalacheck.money.arbitrary._

property("amounts in any currency") {
  import currency.any
  forAll { amount: MonetaryAmount =>
    …
  }
}

property("amounts in €") {
  import currency.byCode.EUR
  forAll { amount: MonetaryAmount =>
    …
  }
}
```

## Contribution

Contributions via GitHub pull requests are gladly accepted from their original
author. Along with any pull requests, please state that the contribution is your
original work and that you license the work to the project under the project's
open source license. Whether or not you state this explicitly, by submitting any
copyrighted material via pull request, email, or other means you agree to
license the material under the project's open source license and warrant that
you have the legal authority to do so.

## License

Copyright 2016 gutefrage.net GmbH

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License.  You may obtain a copy of the
License at <http://www.apache.org/licenses/LICENSE-2.0> or in the `LICENSE` file.

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied.  See the License for the
specific language governing permissions and limitations under the License.
