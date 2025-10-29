package klib.data.type.primitives

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import java.math.BigDecimal as JavaBigDecimal
import java.math.BigInteger as JavaBigInteger

/**
 * Converts [java.math.BigInteger] to [com.ionspin.kotlin.bignum.integer.BigInteger]
 * without loss of precision or rounding.
 */
public fun JavaBigInteger.toKotlinBigInteger(): BigInteger {
    if (this == JavaBigInteger.ZERO) return BigInteger.ZERO

    val sign = when {
        this.signum() > 0 -> Sign.POSITIVE
        this.signum() < 0 -> Sign.NEGATIVE
        else -> Sign.ZERO
    }

    return BigInteger.fromByteArray(abs().toByteArray(), sign)
}

/**
 * Converts [java.math.BigDecimal] to [com.ionspin.kotlin.bignum.decimal.BigDecimal]
 * without loss of precision or rounding.
 */
public fun JavaBigDecimal.toKotlinBigDecimal(): BigDecimal {
    if (this == JavaBigDecimal.ZERO) return BigDecimal.ZERO

    val unscaled = this.unscaledValue().toKotlinBigInteger()
    val scale = this.scale().toLong()
    val exponent = -scale

    return BigDecimal.fromBigIntegerWithExponent(
            bigInteger = unscaled,
            exponent = exponent,
            decimalMode = DecimalMode.DEFAULT,
    )
}

