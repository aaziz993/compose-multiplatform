package klib.data.location.currency

import java.util.Currency as JavaCurrency

public fun JavaCurrency.toCommon(): Currency = Currency.forCode(currencyCode)

public fun Currency.toPlatform(): JavaCurrency = JavaCurrency.getInstance(demonym.toString())
